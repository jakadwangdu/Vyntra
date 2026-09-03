import fs from 'node:fs';
import path from 'node:path';

console.log('⚡ Vyntra build starting...');

const distDir = path.resolve(process.cwd(), 'dist');
if (!fs.existsSync(distDir)) {
  fs.mkdirSync(distDir, { recursive: true });
}

// Ensure public assets exist
const publicDir = path.resolve(process.cwd(), 'public');
if (!fs.existsSync(publicDir)) {
  fs.mkdirSync(publicDir, { recursive: true });
}

console.log('✅ Vyntra build completed successfully.');
