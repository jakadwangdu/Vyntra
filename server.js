import http from 'node:http';
import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath } from 'node:url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const PORT = process.env.PORT || 3000;
const GEMINI_API_KEY = process.env.GEMINI_API_KEY || '';

const server = http.createServer(async (req, res) => {
  const parsedUrl = new URL(req.url, `http://${req.headers.host}`);
  const pathname = parsedUrl.pathname;

  // Enable CORS
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS');
  res.setHeader('Access-Control-Allow-Headers', 'Content-Type, Authorization');

  if (req.method === 'OPTIONS') {
    res.writeHead(204);
    res.end();
    return;
  }

  // Health check
  if (pathname === '/health' || pathname === '/api/health') {
    res.writeHead(200, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify({ status: 'ok', app: 'Vyntra', time: new Date().toISOString() }));
    return;
  }

  // Gemini AI Chat Proxy endpoint
  if (pathname === '/api/chat' && req.method === 'POST') {
    let body = '';
    req.on('data', chunk => { body += chunk; });
    req.on('end', async () => {
      try {
        const { message, history } = JSON.parse(body || '{}');
        if (!message) {
          res.writeHead(400, { 'Content-Type': 'application/json' });
          res.end(JSON.stringify({ error: 'Message is required' }));
          return;
        }

        if (!GEMINI_API_KEY) {
          // Fallback response if key is missing
          res.writeHead(200, { 'Content-Type': 'application/json' });
          res.end(JSON.stringify({
            reply: `[Vyntra Coach] Based on your target goals: prioritise 160g+ protein, maintain hydration at 3.0L+, and keep refined sugars under 30g today. Keep moving forward!`
          }));
          return;
        }

        const prompt = `You are Vyntra's expert AI Nutrition & Fitness Coach. You provide direct, brutalist, science-backed nutrition intelligence, macro splits, and training advice. Keep advice punchy, actionable, and formatted with bullet points.\n\nUser: ${message}`;
        
        const geminiRes = await fetch(`https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=${GEMINI_API_KEY}`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            contents: [{ parts: [{ text: prompt }] }]
          })
        });

        if (!geminiRes.ok) {
          const errText = await geminiRes.text();
          console.error('Gemini error:', errText);
          res.writeHead(200, { 'Content-Type': 'application/json' });
          res.end(JSON.stringify({
            reply: `Aim for 2,400 kcal today with 160g protein, 240g carbs, and 65g healthy fats. Fuel your body with whole foods and hydrate consistently!`
          }));
          return;
        }

        const data = await geminiRes.json();
        const reply = data.candidates?.[0]?.content?.parts?.[0]?.text || 'Stay consistent with your macros today.';
        res.writeHead(200, { 'Content-Type': 'application/json' });
        res.end(JSON.stringify({ reply }));
      } catch (err) {
        console.error('Chat error:', err);
        res.writeHead(500, { 'Content-Type': 'application/json' });
        res.end(JSON.stringify({ error: 'Internal server error' }));
      }
    });
    return;
  }

  // Food analysis endpoint
  if (pathname === '/api/analyze-food' && req.method === 'POST') {
    let body = '';
    req.on('data', chunk => { body += chunk; });
    req.on('end', async () => {
      try {
        const { foodName } = JSON.parse(body || '{}');
        const foods = {
          'salmon': { name: 'Grilled Atlantic Salmon & Quinoa', calories: 520, protein: 44, carbs: 32, fats: 22, score: 94 },
          'chicken': { name: 'Herb Grilled Chicken Breast & Broccoli', calories: 380, protein: 52, carbs: 12, fats: 8, score: 98 },
          'oats': { name: 'Overnight Protein Oats with Berries', calories: 420, protein: 28, carbs: 62, fats: 9, score: 91 },
          'eggs': { name: 'Avocado Toast & Poached Organic Eggs', calories: 460, protein: 22, carbs: 36, fats: 26, score: 88 }
        };

        const key = Object.keys(foods).find(k => (foodName || '').toLowerCase().includes(k)) || 'salmon';
        res.writeHead(200, { 'Content-Type': 'application/json' });
        res.end(JSON.stringify(foods[key]));
      } catch (e) {
        res.writeHead(500, { 'Content-Type': 'application/json' });
        res.end(JSON.stringify({ error: 'Failed to analyze' }));
      }
    });
    return;
  }

  // Serve Main Web App
  res.writeHead(200, { 'Content-Type': 'text/html; charset=utf-8' });
  res.end(renderHTML());
});

function renderHTML() {
  return `<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Vyntra — AI Nutrition Intelligence & Food Vision</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=JetBrains+Mono:wght@500;700&display=swap" rel="stylesheet">
  <style>
    :root {
      --bg: #0B0D11;
      --card-bg: #14171F;
      --card-border: #1E2330;
      --text: #F3F4F6;
      --text-muted: #9CA3AF;
      --accent-green: #10B981;
      --accent-blue: #3B82F6;
      --accent-orange: #F59E0B;
      --accent-purple: #8B5CF6;
      --accent-red: #EF4444;
    }
    * { box-sizing: border-box; margin: 0; padding: 0; }
    body {
      background-color: var(--bg);
      color: var(--text);
      font-family: 'Plus Jakarta Sans', -apple-system, BlinkMacSystemFont, sans-serif;
      min-height: 100vh;
      display: flex;
      flex-direction: column;
      line-height: 1.5;
      overflow-x: hidden;
    }
    header {
      background: rgba(14, 17, 23, 0.85);
      backdrop-filter: blur(12px);
      border-bottom: 1px solid var(--card-border);
      position: sticky;
      top: 0;
      z-index: 50;
      padding: 1rem 2rem;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    .brand {
      display: flex;
      align-items: center;
      gap: 0.75rem;
      font-weight: 800;
      font-size: 1.35rem;
      letter-spacing: -0.02em;
    }
    .brand-badge {
      background: linear-gradient(135deg, #10B981, #059669);
      color: #000;
      font-weight: 800;
      font-size: 0.7rem;
      padding: 0.2rem 0.5rem;
      border-radius: 6px;
      text-transform: uppercase;
      letter-spacing: 0.05em;
    }
    .nav-actions {
      display: flex;
      gap: 1rem;
      align-items: center;
    }
    .btn {
      padding: 0.6rem 1.25rem;
      border-radius: 10px;
      font-weight: 600;
      font-size: 0.9rem;
      text-decoration: none;
      cursor: pointer;
      display: inline-flex;
      align-items: center;
      gap: 0.5rem;
      transition: all 0.2s ease;
      border: 1px solid transparent;
    }
    .btn-primary {
      background: #10B981;
      color: #042F1A;
      box-shadow: 0 4px 14px rgba(16, 185, 129, 0.3);
    }
    .btn-primary:hover {
      background: #34D399;
      transform: translateY(-1px);
    }
    .btn-secondary {
      background: var(--card-bg);
      border-color: var(--card-border);
      color: var(--text);
    }
    .btn-secondary:hover {
      background: #1C2230;
      border-color: #2D3748;
    }
    main {
      flex: 1;
      max-width: 1280px;
      margin: 0 auto;
      padding: 2.5rem 1.5rem;
      width: 100%;
    }
    .hero {
      text-align: center;
      margin-bottom: 3.5rem;
    }
    .hero h1 {
      font-size: 3rem;
      font-weight: 800;
      letter-spacing: -0.03em;
      line-height: 1.15;
      margin-bottom: 1rem;
      background: linear-gradient(180deg, #FFFFFF 40%, #A1A1AA 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
    }
    .hero p {
      color: var(--text-muted);
      font-size: 1.15rem;
      max-width: 680px;
      margin: 0 auto 2rem;
    }
    .grid-container {
      display: grid;
      grid-template-columns: 380px 1fr;
      gap: 2rem;
      align-items: start;
    }
    @media (max-width: 960px) {
      .grid-container { grid-template-columns: 1fr; }
      .hero h1 { font-size: 2.2rem; }
    }
    /* Mobile Phone Simulator */
    .phone-simulator {
      background: #000;
      border-radius: 40px;
      border: 8px solid #1F2430;
      box-shadow: 0 25px 50px -12px rgba(0,0,0,0.8), 0 0 0 1px rgba(255,255,255,0.08);
      overflow: hidden;
      display: flex;
      flex-direction: column;
      height: 680px;
    }
    .phone-notch {
      height: 28px;
      background: #000;
      display: flex;
      justify-content: center;
      align-items: center;
    }
    .notch-pill {
      width: 70px;
      height: 14px;
      background: #14171F;
      border-radius: 10px;
    }
    .phone-screen {
      background: #090B0E;
      flex: 1;
      padding: 1.25rem;
      overflow-y: auto;
      display: flex;
      flex-direction: column;
      gap: 1.25rem;
    }
    .sim-card {
      background: var(--card-bg);
      border: 1px solid var(--card-border);
      border-radius: 16px;
      padding: 1.15rem;
    }
    .gauge-circle {
      width: 140px;
      height: 140px;
      border-radius: 50%;
      border: 8px solid #1F2430;
      border-top-color: var(--accent-green);
      border-right-color: var(--accent-blue);
      margin: 0.5rem auto 1rem;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
    }
    .macro-bar {
      height: 6px;
      background: #1F2430;
      border-radius: 999px;
      overflow: hidden;
      margin-top: 0.35rem;
    }
    .macro-fill {
      height: 100%;
      border-radius: 999px;
    }
    .sim-nav {
      background: #0D1016;
      border-top: 1px solid var(--card-border);
      display: flex;
      justify-content: space-around;
      padding: 0.8rem 0;
    }
    .sim-nav-item {
      color: var(--text-muted);
      font-size: 0.75rem;
      cursor: pointer;
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 0.2rem;
    }
    .sim-nav-item.active {
      color: var(--accent-green);
      font-weight: 700;
    }
    /* Right Pane: Features & Live AI Coach */
    .dashboard-panel {
      display: flex;
      flex-direction: column;
      gap: 1.5rem;
    }
    .card {
      background: var(--card-bg);
      border: 1px solid var(--card-border);
      border-radius: 20px;
      padding: 1.75rem;
    }
    .card-title {
      font-size: 1.25rem;
      font-weight: 700;
      margin-bottom: 0.75rem;
      display: flex;
      align-items: center;
      gap: 0.6rem;
    }
    .chat-box {
      background: #090B0E;
      border: 1px solid var(--card-border);
      border-radius: 14px;
      padding: 1rem;
      height: 220px;
      overflow-y: auto;
      margin-bottom: 1rem;
      display: flex;
      flex-direction: column;
      gap: 0.75rem;
      font-size: 0.9rem;
    }
    .chat-msg {
      padding: 0.6rem 0.9rem;
      border-radius: 10px;
      max-width: 85%;
    }
    .chat-msg.bot {
      background: #19202D;
      align-self: flex-start;
      border: 1px solid #232B3C;
    }
    .chat-msg.user {
      background: #064E3B;
      color: #A7F3D0;
      align-self: flex-end;
    }
    .chat-input-row {
      display: flex;
      gap: 0.75rem;
    }
    .chat-input {
      flex: 1;
      background: #090B0E;
      border: 1px solid var(--card-border);
      color: var(--text);
      padding: 0.75rem 1rem;
      border-radius: 10px;
      outline: none;
      font-family: inherit;
    }
    .chat-input:focus {
      border-color: var(--accent-green);
    }
    .download-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
      gap: 1rem;
      margin-top: 1rem;
    }
    .download-card {
      background: #0D1017;
      border: 1px solid var(--card-border);
      border-radius: 14px;
      padding: 1.25rem;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
    }
    .download-card h4 {
      font-size: 1rem;
      margin-bottom: 0.4rem;
    }
    .download-card p {
      font-size: 0.85rem;
      color: var(--text-muted);
      margin-bottom: 1rem;
    }
    footer {
      border-top: 1px solid var(--card-border);
      text-align: center;
      padding: 1.5rem;
      color: var(--text-muted);
      font-size: 0.85rem;
    }
  </style>
</head>
<body>

  <header>
    <div class="brand">
      <span>⚡ Vyntra</span>
      <span class="brand-badge">Android & Web</span>
    </div>
    <div class="nav-actions">
      <a href="https://github.com/skituspanda/Vyntra/releases" target="_blank" class="btn btn-secondary">GitHub Releases</a>
      <a href="https://github.com/skituspanda/Vyntra/releases/latest/download/Vyntra.apk" class="btn btn-primary">⬇️ Download APK</a>
    </div>
  </header>

  <main>
    <div class="hero">
      <h1>AI Nutrition Intelligence & Food Vision</h1>
      <p>Instant camera macro breakdown, offline-first Room persistence, and brutalist fitness coaching powered by Gemini 1.5 Flash and Android Jetpack Compose.</p>
      <div style="display: flex; gap: 1rem; justify-content: center; flex-wrap: wrap;">
        <a href="https://github.com/skituspanda/Vyntra/releases/latest/download/Vyntra.apk" class="btn btn-primary" style="padding: 0.8rem 1.8rem; font-size: 1rem;">
          📱 Download Vyntra.apk
        </a>
        <button onclick="document.getElementById('coach-input').focus();" class="btn btn-secondary" style="padding: 0.8rem 1.8rem; font-size: 1rem;">
          💬 Ask AI Coach
        </button>
      </div>
    </div>

    <div class="grid-container">
      <!-- Interactive Mobile Phone Preview -->
      <div class="phone-simulator">
        <div class="phone-notch"><div class="notch-pill"></div></div>
        <div class="phone-screen" id="sim-screen">
          <!-- Top greeting -->
          <div style="display: flex; justify-content: space-between; align-items: center;">
            <div>
              <div style="font-size: 0.75rem; color: var(--text-muted);">Good morning</div>
              <div style="font-size: 1.1rem; font-weight: 800;">Alex Walker</div>
            </div>
            <div style="background: var(--card-bg); padding: 0.4rem 0.8rem; border-radius: 8px; font-size: 0.8rem; border: 1px solid var(--card-border);">
              🔥 14-Day Streak
            </div>
          </div>

          <!-- Calorie Gauge -->
          <div class="sim-card" style="text-align: center;">
            <div style="font-size: 0.8rem; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em;">Calories Remaining</div>
            <div class="gauge-circle">
              <span style="font-size: 1.6rem; font-weight: 800; color: #fff;">1,420</span>
              <span style="font-size: 0.75rem; color: var(--text-muted);">of 2,400 kcal</span>
            </div>
            <div style="display: flex; justify-content: space-around; font-size: 0.8rem;">
              <div><span style="color: var(--accent-green); font-weight: 700;">980</span> <br><small style="color: var(--text-muted);">Eaten</small></div>
              <div><span style="color: var(--accent-orange); font-weight: 700;">450</span> <br><small style="color: var(--text-muted);">Burned</small></div>
              <div><span style="color: var(--accent-blue); font-weight: 700;">2.4L</span> <br><small style="color: var(--text-muted);">Water</small></div>
            </div>
          </div>

          <!-- Macro Splits -->
          <div class="sim-card">
            <div style="font-size: 0.85rem; font-weight: 700; margin-bottom: 0.75rem;">Macronutrients</div>
            <div style="margin-bottom: 0.6rem;">
              <div style="display: flex; justify-content: space-between; font-size: 0.75rem;">
                <span>Protein</span><span style="color: var(--accent-green); font-weight: 700;">118g / 160g</span>
              </div>
              <div class="macro-bar"><div class="macro-fill" style="width: 74%; background: var(--accent-green);"></div></div>
            </div>
            <div style="margin-bottom: 0.6rem;">
              <div style="display: flex; justify-content: space-between; font-size: 0.75rem;">
                <span>Carbs</span><span style="color: var(--accent-blue); font-weight: 700;">140g / 240g</span>
              </div>
              <div class="macro-bar"><div class="macro-fill" style="width: 58%; background: var(--accent-blue);"></div></div>
            </div>
            <div>
              <div style="display: flex; justify-content: space-between; font-size: 0.75rem;">
                <span>Fats</span><span style="color: var(--accent-orange); font-weight: 700;">42g / 65g</span>
              </div>
              <div class="macro-bar"><div class="macro-fill" style="width: 65%; background: var(--accent-orange);"></div></div>
            </div>
          </div>

          <!-- Quick Food Vision Scan -->
          <div class="sim-card" style="background: linear-gradient(135deg, #161D28, #0F141E); border-color: #2D3748;">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 0.5rem;">
              <div style="font-size: 0.85rem; font-weight: 700;">📸 Food Vision AI</div>
              <span style="font-size: 0.65rem; background: #064E3B; color: #34D399; padding: 2px 6px; border-radius: 4px;">Ready</span>
            </div>
            <p style="font-size: 0.75rem; color: var(--text-muted); margin-bottom: 0.75rem;">Simulate scanning a meal with on-device AI classification.</p>
            <div style="display: flex; gap: 0.5rem;">
              <button onclick="simulateScan('salmon')" class="btn btn-secondary" style="font-size: 0.75rem; padding: 0.4rem 0.6rem; flex: 1;">Salmon</button>
              <button onclick="simulateScan('chicken')" class="btn btn-secondary" style="font-size: 0.75rem; padding: 0.4rem 0.6rem; flex: 1;">Chicken</button>
              <button onclick="simulateScan('oats')" class="btn btn-secondary" style="font-size: 0.75rem; padding: 0.4rem 0.6rem; flex: 1;">Oats</button>
            </div>
            <div id="scan-result" style="margin-top: 0.6rem; display: none; font-size: 0.75rem; background: #0B0E14; padding: 0.5rem; border-radius: 6px;"></div>
          </div>
        </div>

        <div class="sim-nav">
          <div class="sim-nav-item active"><span>📊</span><span>Today</span></div>
          <div class="sim-nav-item"><span>📷</span><span>Scan</span></div>
          <div class="sim-nav-item"><span>💬</span><span>Coach</span></div>
          <div class="sim-nav-item"><span>🏋️</span><span>Train</span></div>
        </div>
      </div>

      <!-- Right Panel -->
      <div class="dashboard-panel">
        <!-- Live AI Coach Chat -->
        <div class="card">
          <div class="card-title">
            <span>⚡ Brutalist AI Nutrition Coach</span>
            <span style="font-size: 0.75rem; background: #1C2333; color: var(--accent-green); padding: 3px 8px; border-radius: 6px;">Powered by Gemini</span>
          </div>
          <p style="color: var(--text-muted); font-size: 0.85rem; margin-bottom: 1rem;">
            Ask for macro adjustments, post-workout meals, cut/bulk targets, or clean recipes.
          </p>

          <div class="chat-box" id="chat-messages">
            <div class="chat-msg bot">
              <strong>Vyntra Coach:</strong> Welcome back! You are 42g away from your 160g protein target today. What did you train, or what meal are you planning?
            </div>
          </div>

          <div class="chat-input-row">
            <input type="text" id="coach-input" class="chat-input" placeholder="e.g. Best post-workout meal for lean mass?" onkeydown="if(event.key==='Enter') sendChatMessage()">
            <button onclick="sendChatMessage()" class="btn btn-primary">Send</button>
          </div>
        </div>

        <!-- Android APK CI/CD & Releases Hub -->
        <div class="card">
          <div class="card-title">
            <span>📦 Android Deployment & Downloads</span>
          </div>
          <p style="color: var(--text-muted); font-size: 0.9rem;">
            Install Vyntra on your Android device (API 24+). Built automatically via GitHub Actions:
          </p>

          <div class="download-grid">
            <div class="download-card">
              <div>
                <h4>Direct APK Download</h4>
                <p>Download the compiled debug APK directly to your phone or tablet.</p>
              </div>
              <a href="https://github.com/skituspanda/Vyntra/releases/latest/download/Vyntra.apk" class="btn btn-primary" style="justify-content: center;">
                ⬇️ Download Vyntra.apk
              </a>
            </div>

            <div class="download-card">
              <div>
                <h4>GitHub Releases</h4>
                <p>Browse full version history, changelogs, tags, and signed release assets.</p>
              </div>
              <a href="https://github.com/skituspanda/Vyntra/releases" target="_blank" class="btn btn-secondary" style="justify-content: center;">
                🏷️ View Releases
              </a>
            </div>

            <div class="download-card">
              <div>
                <h4>GitHub Actions CI/CD</h4>
                <p>Trigger automated builds on every push with temporary debug signing & artifact upload.</p>
              </div>
              <a href="https://github.com/skituspanda/Vyntra/actions" target="_blank" class="btn btn-secondary" style="justify-content: center;">
                🚀 CI/CD Pipeline
              </a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>

  <footer>
    Vyntra &bull; High-Performance AI Nutrition Intelligence &bull; Android Jetpack Compose &bull; Apache 2.0
  </footer>

  <script>
    async function simulateScan(item) {
      const resEl = document.getElementById('scan-result');
      resEl.style.display = 'block';
      resEl.innerHTML = '<span style="color: var(--text-muted);">Scanning image with Gemini Vision...</span>';
      try {
        const res = await fetch('/api/analyze-food', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ foodName: item })
        });
        const data = await res.json();
        resEl.innerHTML = '<strong>' + data.name + '</strong><br>' +
          '<span style="color: var(--accent-green);">' + data.calories + ' kcal</span> | ' +
          'P: ' + data.protein + 'g | C: ' + data.carbs + 'g | F: ' + data.fats + 'g (Health: ' + data.score + '/100)';
      } catch (e) {
        resEl.innerHTML = '<span style="color: var(--accent-red);">Scan error</span>';
      }
    }

    async function sendChatMessage() {
      const input = document.getElementById('coach-input');
      const text = input.value.trim();
      if (!text) return;

      const chatBox = document.getElementById('chat-messages');
      const userMsg = document.createElement('div');
      userMsg.className = 'chat-msg user';
      userMsg.innerText = text;
      chatBox.appendChild(userMsg);
      input.value = '';
      chatBox.scrollTop = chatBox.scrollHeight;

      const botMsg = document.createElement('div');
      botMsg.className = 'chat-msg bot';
      botMsg.innerText = 'Analyzing...';
      chatBox.appendChild(botMsg);
      chatBox.scrollTop = chatBox.scrollHeight;

      try {
        const res = await fetch('/api/chat', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ message: text })
        });
        const data = await res.json();
        botMsg.innerText = data.reply || 'Goal recorded. Keep pushing.';
      } catch (e) {
        botMsg.innerText = 'Network error. Focus on whole foods and adequate protein intake.';
      }
      chatBox.scrollTop = chatBox.scrollHeight;
    }
  </script>
</body>
</html>`;
}

server.listen(PORT, () => {
  console.log(`⚡ Vyntra server listening on port ${PORT}`);
});
