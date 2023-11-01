// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
    devtools: {enabled: false},
    modules: ['@element-plus/nuxt', '@nuxtjs/supabase'],
    elementPlus: {
        importStyle: 'scss',
    },
    css: ["~/assets/css/main.scss"]
});
