import { useContext, useEffect, useState } from 'react';
import CatService from '../../services/CatService';
import { Link } from 'react-router-dom';
import { UserContext } from '../../context/UserContext';
import BookmarkService from '../../services/BookmarkService';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import styles from './CatsView.module.css';
import LoadingSpinner from '../../components/LoadingSpinner/LoadingSpinner';

export default function CatsView() {
	const { user } = useContext(UserContext);
  const [cats, setCats] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [errorMsg, setErrorMsg] = useState('');
  const [sortKey, setSortKey] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const [viewMode, setViewMode] = useState('card');
  const [bookmarkedIds, setBookmarkedIds] = useState(new Set());


	useEffect(() => {
  		setIsLoading(true);

  		CatService.getAll()
    	.then(response => {
      		setCats(response.data);
    	})
    	.catch(error => {
      		const message =
				error?.response?.data?.message ||
				error?.message ||
				'Unable to load cats.';
      		setErrorMsg(message);
    	})
    	.finally(() => setIsLoading(false));
	}, []);



	useEffect(() => {
		const delay = setTimeout(() => {
			const params = {};
			if (searchTerm.trim()) {
    		params.q = searchTerm.trim();
  			}
			if (sortKey) {
				params.sort = sortKey;
			}

  		CatService.getAll(params)
    		.then(response => {
      		setCats(response.data);
    	})
    	.catch(error => {
      	const message =
        error?.response?.data?.message ||
        error?.message ||
        'Unable to search cats.';
      setErrorMsg(message);
	});

	}, 300); 

	return () => clearTimeout(delay);
	}, [searchTerm, sortKey]);



useEffect(() => {
	if (!user) return;

	BookmarkService.getMine()
		.then(res => {
			setBookmarkedIds(new Set(res.data.map(b => b.catId)));
		})
		.catch(() => {});
}, [user]);

async function handleBookmarkToggle(catId) {
  const isBookmarked = bookmarkedIds.has(catId);

  try {
    if (isBookmarked) {
      await BookmarkService.remove(catId); // DELETE
      setBookmarkedIds(prev => {
        const next = new Set(prev);
        next.delete(catId);
        return next;
      });
    } else {
      await BookmarkService.add(catId); // POST
      setBookmarkedIds(prev => new Set(prev).add(catId));
    }
  } catch {
    alert('Failed to update bookmark');
  }
}

if (errorMsg) {
	return (
		<div className={styles.container}>
			<h1>All Cats</h1>
			<p className={styles.error}>{errorMsg}</p>
		</div>
	);
}

console.log("cats:", cats);
return (
	
	<div className={styles.page}>
	<div className={styles.container}>
		<h1>
			<FontAwesomeIcon icon="cat" className={styles.catIcon} />
			All Cats {" "}  
			<FontAwesomeIcon icon="cat" className={styles.catIcon} />
		</h1>
		
		


		{!user && (
			<p className={styles.loginPrompt}>
			Welcome! <Link to="/login">Login</Link> to bookmark or hire cats.</p>
		)}

		<div className={styles.controls}>
			<form className={styles.searchForm}>
				<input
					type="text"
					placeholder="Search by name, color, fur length, skills, or description..."
					value={searchTerm}
					onChange={(e) => setSearchTerm(e.target.value)}
					className={styles.searchInput}
				/>
				<button type="submit" className={styles.searchButton}>
					<FontAwesomeIcon icon="magnifying-glass" />
				</button>
			</form>

			<select
				value={sortKey}
				onChange={(e) => setSortKey(e.target.value)}
				className={styles.sortSelect}
			>
				<option value="">Sort By</option>
				<option value="name">Name</option>
				<option value="age">Age</option>
				<option value="color">Color</option>
				<option value="featured">Featured</option>
				<option value="rating">Highest Rated</option>
			</select>

			{user?.role === 'ROLE_STAFF' && (
		<div className={styles.staffActions}>
			<Link to="/admin/cats/create" className={styles.createButton}>
			<FontAwesomeIcon icon="plus" /> Add New Cat
			</Link>
		</div>
		)}

			<div className={styles.viewToggle}>
				<FontAwesomeIcon
					icon="grip"
					className={`${styles.viewIcon} ${viewMode === 'card' ? styles.active : ''}`}
					onClick={() => setViewMode('card')}
					title="Card View"
				/>
				<FontAwesomeIcon
					icon="table"
					className={`${styles.viewIcon} ${viewMode === 'table' ? styles.active : ''}`}
					onClick={() => setViewMode('table')}
					title="Table View"
				/>
			</div>
		</div>

		{isLoading ? ( 
			<div className={styles.loading}>
				<LoadingSpinner />
			</div>
		) : cats.length === 0 ? (
			<p className={styles.empty}>No cats found matching your search.</p>
		) : (
			<>
				{viewMode === 'card' ? (
					<div className={styles.cardGrid}>
						{cats.map(cat => (
							<div key={cat.catId} className={styles.card}>
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
										<Link to={`/cats/${cat.catId}`}>{cat.name}</Link>
										{cat.featured && (
											<div className={styles.featuredBadge}>
                								<FontAwesomeIcon icon="crown" /> Featured
											</div>
										)}
									</h3>

									{cat.skills && (
										<p className={styles.skills}>
											<strong>Skills:</strong> {cat.skills.substring(0, 100)}
											{cat.skills.length > 100 && '...'}
										</p>
									)}
									
									{cat.description && (
										<p className={styles.description}>
											{cat.description.substring(0, 80)}
											{cat.description.length > 80 && '...'}
										</p>
									)}

									{cat.rankScore && (
										<p className={styles.rank}>
											<FontAwesomeIcon icon="star" /> Average Rating: {cat.rankScore}
										</p>
									)}

									<div className={styles.catInfo}>
										{cat.age && <p className={styles.color}>
											<FontAwesomeIcon icon="cat" />Age:{cat.age}
											</p>}
									</div>

									

									

									

									{user && (
									<button
										className={styles.bookmarkButton}
										onClick={() => handleBookmarkToggle(cat.catId)}
										title={bookmarkedIds.has(cat.catId) ? 'Remove bookmark' : 'Bookmark this cat'}
									>
										<FontAwesomeIcon
										icon={bookmarkedIds.has(cat.catId) ? 'bookmark' : ['far', 'bookmark']}
										/>
										{bookmarkedIds.has(cat.catId) ? ' Bookmarked' : ' Bookmark'}
									</button>
									)}


								</div>
							</div>
						))}
				</div>
			) : (
				<table className={styles.table}>
					<thead>
						<tr>
							<th>Name</th>
							<th>Age</th>
							<th>Color</th>
							
							<th>Skills</th>
							{user && <th>Bookmark</th>}
						</tr>
					</thead>
					<tbody>
						{cats.map(cat => (
							<tr key={cat.catId}>
								<td>
									<Link to={`/cats/${cat.catId}`}>
										{cat.name}
										{cat.featured && <FontAwesomeIcon icon="star" />}
									</Link>
								</td>
								<td>{cat.age || '-'}</td>
								<td>{cat.color || '-'}</td>
								
								<td className={styles.skillsCell}>
									{cat.skills ? cat.skills.substring(0, 50) + (cat.skills.length > 50 ? '...' : '') : '-'}
								</td>
								{user && (
									<td>
										<button
											className={styles.tableButton}
											onClick={() => handleBookmarkToggle(cat.catId)}
											title="Bookmark this cat"
										>
											<FontAwesomeIcon icon={["far", "bookmark"]} />
										</button>
									</td>
								)}
							</tr>
						))}
					</tbody>
				</table>
			)}
		</>
		)}
	</div>
	

	
	</div>
);
}



