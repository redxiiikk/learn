// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  devtools: { enabled: false },
  modules: ['@element-plus/nuxt'],
  elementPlus: {
    importStyle: 'scss',
  },
  css: ["~/assets/css/main.scss"]
});
