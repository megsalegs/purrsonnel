import axios from 'axios';

export default {
  getMine() {
    return axios.get('/bookmarks');
  },
  add(catId) {
    return axios.post(`/bookmarks/${catId}`);
  },
  remove(catId) {
    return axios.delete(`/bookmarks/${catId}`);
  }
};
