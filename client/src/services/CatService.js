import axios from 'axios';



export default {
  getAll(params = {}) {
    return axios.get('/cats', { params });
  },

  getById(catId) {
    return axios.get(`/cats/${catId}`);
  },

  create(cat) {
    return axios.post('/cats', cat);
  },

  update(catId, cat) {
    return axios.put(`/cats/${catId}`, cat);
  },

  remove(catId) {
    return axios.delete(`/cats/${catId}`);
  },

  	getFeatured() {
		return axios.get('/cats/featured');
	},
};
