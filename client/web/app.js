"use strict";

// Mirrors game-service's session.go `sessionDuration` const (2m30s). The
// server is the real authority on when a session ends (it sends
// session_ended, which stops the timer regardless of this value) — this is
// only used to render a countdown, not to end the match client-side.
const MATCH_DURATION_SECONDS = 150;

const state = {
  token: null,
  username: null,
  rating: null,
  ws: null,
  sessionId: null,
  opponentId: null,
  opponentLabel: "Opponent",
  matchStartedAt: null,
  elapsedTimer: null,
  seenFlags: 0,
  target: null,
  spawnTimer: null,
};

// ---------- screen management ----------

const screens = [
  "screen-auth",
  "screen-lobby",
  "screen-queue",
  "screen-game",
  "screen-result",
  "screen-leaderboard",
];

function showScreen(id) {
  for (const s of screens) {
    document.getElementById(s).classList.toggle("visible", s === id);
  }
}

function setLoggedInChrome(loggedIn) {
  document.getElementById("topnav").hidden = !loggedIn;
  document.getElementById("whoami").textContent = loggedIn ? state.username : "";
}

// ---------- auth ----------

for (const tab of document.querySelectorAll(".tab")) {
  tab.addEventListener("click", () => {
    for (const t of document.querySelectorAll(".tab")) t.classList.remove("active");
    for (const p of document.querySelectorAll(".tab-panel")) p.classList.remove("active");
    tab.classList.add("active");
    document.getElementById(`form-${tab.dataset.tab}`).classList.add("active");
  });
}

document.getElementById("form-login").addEventListener("submit", async (e) => {
  e.preventDefault();
  const username = document.getElementById("login-username").value.trim();
  const password = document.getElementById("login-password").value;
  const errorEl = document.getElementById("login-error");
  errorEl.textContent = "";

  try {
    const res = await fetch("/api/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password }),
    });
    if (!res.ok) throw new Error("Invalid username or password");
    const body = await res.json();
    await onAuthenticated(username, body.token);
  } catch (err) {
    errorEl.textContent = err.message;
  }
});

document.getElementById("form-register").addEventListener("submit", async (e) => {
  e.preventDefault();
  const username = document.getElementById("register-username").value.trim();
  const email = document.getElementById("register-email").value.trim();
  const password = document.getElementById("register-password").value;
  const errorEl = document.getElementById("register-error");
  errorEl.textContent = "";

  try {
    const res = await fetch("/api/auth/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, email, password }),
    });
    if (!res.ok) throw new Error("Registration failed (username/email may be taken)");
    const body = await res.json();
    await onAuthenticated(username, body.token);
  } catch (err) {
    errorEl.textContent = err.message;
  }
});

async function onAuthenticated(username, token) {
  state.username = username;
  state.token = token;
  setLoggedInChrome(true);
  await refreshRating();
  showScreen("screen-lobby");
}

async function refreshRating() {
  try {
    const res = await fetch("/api/users/me", {
      headers: { Authorization: `Bearer ${state.token}` },
    });
    if (!res.ok) return;
    const body = await res.json();
    state.rating = body.rating;
    document.getElementById("lobby-rating").textContent = body.rating;
  } catch {
    // leaderboard/rating display is best-effort, not worth surfacing an error for
  }
}

document.getElementById("nav-logout").addEventListener("click", () => {
  closeWs();
  state.token = null;
  state.username = null;
  setLoggedInChrome(false);
  showScreen("screen-auth");
});

// requireAuth guards every navigation that assumes a logged-in session.
// Hiding the nav button via [hidden] stops the normal click path, but this
// is the actual authority check — without it, anything that lands on an
// authenticated screen while logged out (a stale reference, a race, a typo
// in a future edit) sends the user to a broken "logged in" looking screen
// instead of back to the login form.
function requireAuth(thenShow) {
  if (!state.token) {
    showScreen("screen-auth");
    return;
  }
  showScreen(thenShow);
}

// ---------- lobby / queue ----------

document.getElementById("btn-join-queue").addEventListener("click", () => {
  if (!state.token) {
    showScreen("screen-auth");
    return;
  }
  openWs();
  showScreen("screen-queue");
  document.getElementById("queue-ticket").textContent = "";
});

document.getElementById("btn-leave-queue").addEventListener("click", () => {
  send({ type: "leave_queue" });
  closeWs();
  requireAuth("screen-lobby");
});

document.getElementById("nav-leaderboard").addEventListener("click", loadLeaderboard);
document.getElementById("btn-leaderboard-back").addEventListener("click", () => requireAuth("screen-lobby"));
document.getElementById("btn-play-again").addEventListener("click", () => requireAuth("screen-lobby"));

// ---------- websocket ----------

function openWs() {
  const proto = location.protocol === "https:" ? "wss" : "ws";
  const ws = new WebSocket(`${proto}://${location.host}/ws`);
  state.ws = ws;

  ws.addEventListener("open", () => {
    send({ type: "auth", token: state.token, player_id: state.username });
    send({ type: "join_queue", rating: state.rating || 1000 });
  });

  ws.addEventListener("message", (event) => {
    const msg = JSON.parse(event.data);
    handleServerMessage(msg);
  });

  ws.addEventListener("close", () => {
    state.ws = null;
  });
}

function closeWs() {
  if (state.ws) {
    state.ws.close();
    state.ws = null;
  }
}

function send(obj) {
  if (state.ws && state.ws.readyState === WebSocket.OPEN) {
    state.ws.send(JSON.stringify(obj));
  }
}

function handleServerMessage(msg) {
  switch (msg.type) {
    case "queued":
      document.getElementById("queue-ticket").textContent = `Ticket ${msg.ticket_id.slice(0, 8)}`;
      break;
    case "match_found":
      state.sessionId = msg.session_id;
      state.opponentId = msg.player_ids.find((id) => id !== state.username) || "opponent";
      state.opponentLabel = state.opponentId;
      state.seenFlags = 0;
      startMatch();
      break;
    case "session_update":
      applySessionUpdate(msg);
      break;
    case "error":
      console.error("gateway error:", msg.message);
      break;
  }
}

// ---------- game screen ----------

function startMatch() {
  document.getElementById("opp-label").textContent = state.opponentLabel;
  document.getElementById("score-me").textContent = "0";
  document.getElementById("score-opp").textContent = "0";
  showScreen("screen-game");

  state.matchStartedAt = Date.now();
  tickCountdown();
  state.elapsedTimer = setInterval(tickCountdown, 1000);

  initCanvas();
  scheduleNextTarget(300);
}

function tickCountdown() {
  const elapsed = Math.floor((Date.now() - state.matchStartedAt) / 1000);
  const remaining = Math.max(0, MATCH_DURATION_SECONDS - elapsed);
  const m = Math.floor(remaining / 60);
  const s = String(remaining % 60).padStart(2, "0");
  document.getElementById("game-elapsed").textContent = `${m}:${s}`;
}

function applySessionUpdate(msg) {
  for (const entry of msg.scores) {
    if (entry.player_id === state.username) {
      document.getElementById("score-me").textContent = entry.score;
    } else {
      document.getElementById("score-opp").textContent = entry.score;
    }
  }

  if (msg.flags && msg.flags.length > state.seenFlags) {
    const newest = msg.flags[msg.flags.length - 1];
    showFlagToast(newest);
    state.seenFlags = msg.flags.length;
  }

  if (msg.session_ended) {
    endMatch();
  }
}

function showFlagToast(flag) {
  const el = document.getElementById("flag-toast");
  const who = flag.player_id === state.username ? "You were" : `${flag.player_id} was`;
  el.textContent = `⚠ anti-cheat: ${who} flagged (${flag.rule})`;
  el.hidden = false;
  clearTimeout(el._hideTimer);
  el._hideTimer = setTimeout(() => (el.hidden = true), 3500);
}

function endMatch() {
  clearInterval(state.elapsedTimer);
  clearTimeout(state.spawnTimer);
  state.target = null;
  closeWs();

  const me = Number(document.getElementById("score-me").textContent);
  const opp = Number(document.getElementById("score-opp").textContent);
  const heading = document.getElementById("result-heading");
  heading.classList.remove("win", "loss");

  if (me > opp) {
    heading.textContent = "You won";
    heading.classList.add("win");
  } else if (me < opp) {
    heading.textContent = "You lost";
    heading.classList.add("loss");
  } else {
    heading.textContent = "Draw";
  }
  document.getElementById("result-score").textContent = `${me} — ${opp} vs ${state.opponentLabel}`;
  showScreen("screen-result");

  // rating update is applied asynchronously by account-service after the
  // session ends, so give it a moment before refreshing the displayed rating
  setTimeout(refreshRating, 1500);
}

// ---------- canvas reaction-target game ----------
//
// Purely a client-side visual layer: the backend scores every valid
// PlayerAction equally (see game-service's Session.handle), it doesn't know
// or care about this target. Clicking the target just triggers one action
// send, giving the abstract "send actions fast/consistently without being
// flagged" scoring model a concrete, satisfying interaction.

let ctx;

function initCanvas() {
  const canvas = document.getElementById("game-canvas");
  ctx = canvas.getContext("2d");
  canvas.onclick = onCanvasClick;
  requestAnimationFrame(renderLoop);
}

function scheduleNextTarget(delay) {
  clearTimeout(state.spawnTimer);
  state.spawnTimer = setTimeout(spawnTarget, delay);
}

function spawnTarget() {
  const canvas = document.getElementById("game-canvas");
  const r = 26;
  state.target = {
    x: r + Math.random() * (canvas.width - 2 * r),
    y: r + Math.random() * (canvas.height - 2 * r),
    r,
    bornAt: performance.now(),
  };
}

function onCanvasClick(e) {
  if (!state.target) return;
  const canvas = document.getElementById("game-canvas");
  const rect = canvas.getBoundingClientRect();
  const scaleX = canvas.width / rect.width;
  const scaleY = canvas.height / rect.height;
  const x = (e.clientX - rect.left) * scaleX;
  const y = (e.clientY - rect.top) * scaleY;

  const dx = x - state.target.x;
  const dy = y - state.target.y;
  if (Math.sqrt(dx * dx + dy * dy) <= state.target.r) {
    send({ type: "action" });
    state.target = null;
    scheduleNextTarget(150 + Math.random() * 350);
  }
}

function renderLoop(now) {
  if (!ctx) return;
  const canvas = ctx.canvas;
  ctx.clearRect(0, 0, canvas.width, canvas.height);

  ctx.strokeStyle = "#1a2333";
  ctx.lineWidth = 1;
  for (let x = 0; x < canvas.width; x += 40) {
    ctx.beginPath();
    ctx.moveTo(x, 0);
    ctx.lineTo(x, canvas.height);
    ctx.stroke();
  }
  for (let y = 0; y < canvas.height; y += 40) {
    ctx.beginPath();
    ctx.moveTo(0, y);
    ctx.lineTo(canvas.width, y);
    ctx.stroke();
  }

  if (state.target) {
    const age = now - state.target.bornAt;
    const pulse = 1 + 0.08 * Math.sin(age / 120);
    const r = state.target.r * pulse;

    const grad = ctx.createRadialGradient(state.target.x, state.target.y, 0, state.target.x, state.target.y, r * 1.8);
    grad.addColorStop(0, "rgba(94,234,212,0.9)");
    grad.addColorStop(1, "rgba(94,234,212,0)");
    ctx.fillStyle = grad;
    ctx.beginPath();
    ctx.arc(state.target.x, state.target.y, r * 1.8, 0, Math.PI * 2);
    ctx.fill();

    ctx.fillStyle = "#5eead4";
    ctx.beginPath();
    ctx.arc(state.target.x, state.target.y, r, 0, Math.PI * 2);
    ctx.fill();
  }

  if (document.getElementById("screen-game").classList.contains("visible")) {
    requestAnimationFrame(renderLoop);
  }
}

// ---------- leaderboard ----------

async function loadLeaderboard() {
  if (!state.token) {
    showScreen("screen-auth");
    return;
  }
  showScreen("screen-leaderboard");
  const body = document.getElementById("leaderboard-body");
  body.innerHTML = "";

  try {
    const res = await fetch("/api/leaderboard", {
      headers: { Authorization: `Bearer ${state.token}` },
    });
    if (!res.ok) return;
    const entries = await res.json();
    for (const entry of entries) {
      const tr = document.createElement("tr");
      if (entry.username === state.username) tr.classList.add("me");
      tr.innerHTML = `<td>${entry.rank}</td><td>${entry.username}</td><td>${entry.rating}</td>`;
      body.appendChild(tr);
    }
  } catch {
    // best-effort
  }
}

showScreen("screen-auth");
