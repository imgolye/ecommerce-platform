import axios from "axios";
import { useUserStore } from "@/stores/user";

const http = axios.create({
  baseURL: "/api",
  timeout: 15000
});

http.interceptors.request.use((config) => {
  const userStore = useUserStore();
  if (userStore.token) {
    config.headers.Authorization = `Bearer ${userStore.token}`;
  }
  return config;
});

http.interceptors.response.use(
  (response) => response.data,
  (error) => Promise.reject(error)
);

export default http;
