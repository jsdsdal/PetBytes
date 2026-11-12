import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'
import { defineConfig } from 'vite'
import { fileURLToPath, URL } from 'node:url'

export default defineConfig({
  // react() forstår JSX, tailwindcss() forstår Tailwind-klasser
  plugins: [react(), tailwindcss()],
  // Samme @-genvej som i jsconfig.json, men nu for Vite
  resolve: {
    alias: { '@': fileURLToPath(new URL('./src', import.meta.url)) },
  },
  // Sender alle /api kald videre til Spring Boot på port 8080
  server: { proxy: { '/api': 'http://localhost:8080' } },
})