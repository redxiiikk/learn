<script setup lang="ts">
import {ElNotification} from "element-plus";

const supabase = useSupabaseClient();

const form = reactive({
  email: "",
  password: ""
})

async function onLogin() {
  const {error} = await supabase.auth.signInWithPassword({
    email: form.email,
    password: form.password,
  })

  if (error) {
    ElNotification({
      title: 'Login Failed',
      message: error.message,
      type: 'error',
    })
  } else {
    navigateTo("/")
  }
}

</script>

<template>
  <el-form :model="form" label-width="120px">
    <el-form-item label="Email">
      <el-input v-model="form.email"/>
    </el-form-item>
    <el-form-item label="Password">
      <el-input v-model="form.password" type="password"/>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onLogin">Login</el-button>
    </el-form-item>
  </el-form>
</template>

<style scoped>
.el-form {
  width: 400px;
}
</style>