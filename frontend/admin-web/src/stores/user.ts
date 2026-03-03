import { defineStore } from "pinia";

type UserState = {
  token: string;
  username: string;
};

const TOKEN_KEY = "admin_token";
const USERNAME_KEY = "admin_username";

export const useUserStore = defineStore("user", {
  state: (): UserState => ({
    token: localStorage.getItem(TOKEN_KEY) ?? "",
    username: localStorage.getItem(USERNAME_KEY) ?? ""
  }),
  getters: {
    isLoggedIn: (state) => Boolean(state.token)
  },
  actions: {
    login(payload: { username: string; token: string }) {
      this.username = payload.username;
      this.token = payload.token;
      localStorage.setItem(TOKEN_KEY, payload.token);
      localStorage.setItem(USERNAME_KEY, payload.username);
    },
    logout() {
      this.username = "";
      this.token = "";
      localStorage.removeItem(TOKEN_KEY);
      localStorage.removeItem(USERNAME_KEY);
    }
  }
});
