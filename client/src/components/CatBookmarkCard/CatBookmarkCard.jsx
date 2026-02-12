import { Link } from 'react-router-dom';
import styles from './CatBookmarkCard.module.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';


export default function CatCard({ cat, onRemove }) {
	return (
		<div className={styles.card}>
		<div className={styles.cardImage}>
			<Link to={`/cats/${cat.catId}`}>
				{cat.primaryImagePath ? (
					<img
					src={
						cat.primaryImagePath.startsWith('http')
						? cat.primaryImagePath
						: `http://localhost:9000${cat.primaryImagePath}`
					}
					alt={cat.name}
					className={styles.catImage}
					/>
				) : (
					<div className={styles.placeholderImage}>🐱</div>
				)}
			</Link>
			</div>


			<div className={styles.cardContent}>
				<h3>
					<Link to={`/cats/${cat.catId}`}>
						<FontAwesomeIcon icon="paw" />{" "}{cat.name}
					</Link>
				</h3>

				{cat.skills && (
					<p className={styles.skills}>
						{cat.skills.substring(0, 80)}
						{cat.skills.length > 80 && '...'}
					</p>
				)}

				{onRemove && (
					<button
						className={styles.removeBtn}
						onClick={() => onRemove(cat.catId)}
					>
						Remove Bookmark
					</button>
				)}
			</div>
		</div>
	);
}