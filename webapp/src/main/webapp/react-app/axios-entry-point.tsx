import axios from "axios";

const instance = axios.create({
  // baseUrl should be replaced by real baseUrl
  baseURL: "http://localhost:4000"
});

export default instance;
