import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import BookmarkService from '../../services/BookmarkService';
import LoadingSpinner from '../../components/LoadingSpinner/LoadingSpinner';
import styles from './BookmarksView.module.css';
import CatBookmarkCard from '../../components/CatBookmarkCard/CatBookmarkCard';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export default function BookmarksView() {
  const [cats, setCats] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  function loadBookmarks() {
    setLoading(true);
    setError('');
    
    BookmarkService.getMine()
      .then(res => setCats(res.data))
      .catch(err => setError(err?.response?.data?.message || 'Unable to load bookmarks'))
      .finally(() => setLoading(false));
  }

  useEffect(() => {
    loadBookmarks();
  }, []);

  function handleRemove(catId) {
    if (!window.confirm('Remove this bookmark?')) return;
    
    BookmarkService.remove(catId)
      .then(loadBookmarks)
      .catch(err => alert('Failed to remove bookmark'));
  }

  if (loading) {
    return (
      <div className={styles.container}>
        <p className={styles.loading}>
          Loading bookmarks... <LoadingSpinner />
        </p>
      </div>
    );
  }

  if (error) {
    return (
      <div className={styles.container}>
        <h1>My Bookmarked Cats</h1>
        <p className={styles.error}>{error}</p>
      </div>
    );
  }

  return (
    <div className={styles.page}>
    <div className={styles.container}>
      <div className={styles.header}>
       <h1>
          <FontAwesomeIcon icon={["far", "bookmark"]} className={styles.bookmarkIcon} />
          Bookmarked Cats
        </h1>
      
      {cats.length === 0 ? (
        <div className={styles.empty}>
          <span className={styles.emptyIcon}>🔖</span>
          <p>No bookmarks yet.</p>
          <Link to="/cats" className={styles.browseLink}>
            Browse Cats to Bookmark
          </Link>
        </div>
      ) : (
        <div className={styles.grid}>
          {cats.map(cat => (
            <CatBookmarkCard
              key={cat.catId}
              cat={cat}
              onRemove={handleRemove}
            />
          ))}
        </div>
      )}
    </div>
    </div>
    </div>
     );
}