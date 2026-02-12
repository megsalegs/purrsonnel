import { useEffect, useState } from "react";
import ReviewService from "../../services/ReviewService";
import styles from './ManageReviewsView.module.css';
import CatService from "../../services/CatService";

export default function ManageReviewsView() {
	const [cats, setCats] = useState([]);
	const [selectedCatId, setSelectedCatId] = useState('');
	const [reviews, setReviews] = useState([]);
	const [loading, setLoading] = useState(false); 
	const [error, setError] = useState('');
	const [editing, setEditing] = useState(null);

	useEffect(() => {
		CatService.getAll()
		.then(res => setCats(res.data))
		.catch(() => setError('Failed to load cats'));
	}, []);

	function loadReviews() {
	if (!selectedCatId) return;

	setLoading(true);
	setError('');

	ReviewService.getByCatId(selectedCatId)
		.then(res => setReviews(res.data))
		.catch(err => setError(err.message || 'Failed to load reviews'))
		.finally(() => setLoading(false));
	}


	function startEdit(review) {
		setEditing({ ...review });
	}

	function cancelEdit() {
		setEditing(null);
	}

	function saveEdit() {
		ReviewService.update(selectedCatId, editing.reviewId, {
			reviewBody: editing.reviewBody,
			rating: editing.rating
		})
		.then(loadReviews)
		.then(() => setEditing(null))
		.catch(err => setError(err.message));
	}

	function deleteReview(reviewId) {
		if (!window.confirm('Delete this review?')) return;
		
		ReviewService.remove(selectedCatId, reviewId)
		.then(loadReviews)
		.catch(err => setError(err.message));
	}

	return (
		<div className={styles.page}>
		<div className={styles.container}>
			<h1>Manage Reviews</h1>

			<div className={styles.filter}>
			<label>
				Cat:
				<select
					value={selectedCatId}
					onChange={e => setSelectedCatId(e.target.value)}
				>
				<option value="">Select a cat</option>
					{cats.map(cat => (
					<option key={cat.catId} value={cat.catId}>
						{cat.name}
					</option>
					))}
				</select>
			</label>

				<button 
				className={styles.loadBtn}
				onClick={loadReviews}>Load Reviews</button>
			</div>

			{loading && <p>Loading...</p>}
			{error && <p className={styles.error}>{error}</p>}

			{reviews.map(review => (
				<div key={review.reviewId} className={styles.card}>
					{editing?.reviewId === review.reviewId ? (
						<>
						<textarea
							value={editing.reviewBody}
							onChange={e =>
							setEditing({ ...editing, reviewBody: e.target.value })
							}
						/>
						<input
						type="number"
						min="1"
						max="5"
						value={editing.rating}
						onChange={e =>
						setEditing({ ...editing, rating: Number(e.target.value) })
						}
						/>
						<button onClick={saveEdit}>Save</button>
						<button onClick={cancelEdit}>Cancel</button>
						</>
					) : (
						<>
							<p>{review.reviewBody}</p>
							<p>Rating: {review.rating ?? '-'}</p>
							<div className={styles.actions}>
								<button onClick={() => startEdit(review)}>Edit</button>
								<button onClick={() => deleteReview(review.reviewId)}>Delete</button>
							</div>
						</>
					)}
			</div>
		))}
	</div>
	<footer className={styles.footer}>
		&copy; Purrsonnel
	</footer>
	</div>
	);
}