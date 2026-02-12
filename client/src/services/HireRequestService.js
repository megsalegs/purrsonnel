import axios from 'axios';

export default {
	getMine() {
		return axios.get('/hire-requests/mine');
	},

	getAll() {
		return axios.get('/hire-requests');
	},

	create({ catId, requestedStartDate, requestedEndDate }) {
		return axios.post('/hire-requests', { catId, requestedStartDate, requestedEndDate });
	},

	cancel(hireRequestId) {
		return axios.put(`/hire-requests/${hireRequestId}/cancel`);
	},

	updateStatus(hireRequestId, status) {
		return axios.put(`/hire-requests/${hireRequestId}/status`, { status });
	}
};
