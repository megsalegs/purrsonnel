import axios from 'axios';

export default {
  getByCatId(catId) {
    return axios.get(`/cats/${catId}/images`);
  },

  create(catId, image) {
    return axios.post(`/cats/${catId}/images`, image);
  },

  delete(catId, imageId) {
    return axios.delete(`cats/${catId}/images/${imageId}`);
  },

  setPrimary(catId, imageId) {
    return axios.put(`cats/${catId}/images/${imageId}/primary`)
  }
};
