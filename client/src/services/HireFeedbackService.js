import axios from "axios";

export default {
	create(feedback) {
		return axios.post('/hire-requests/${hireRequestId}/feedback', feedback);
	}
};