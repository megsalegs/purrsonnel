import axios from 'axios';

export default {
  getByCatId(catId) {
    return axios.get(`/cats/${catId}/reviews`);
  },

  create(catId, review) {
  return axios.post(`/cats/${catId}/reviews`, review);
  },

  update(catId, reviewId, review) {
    return axios.put(`/cats/${catId}/reviews/${reviewId}`, review);
  },

  remove(catId, reviewId) {
    return axios.delete(`/cats/${catId}/reviews/${reviewId}`);
  }
};
