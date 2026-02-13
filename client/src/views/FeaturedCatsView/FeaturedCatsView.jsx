import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import CatService from '../../services/CatService';
import LoadingSpinner from '../../components/LoadingSpinner/LoadingSpinner';
import styles from './FeaturedCatsView.module.css';

export default function FeaturedCatsView() {
  const [cats, setCats] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [errorMsg, setErrorMsg] = useState('');

  useEffect(() => {
    setIsLoading(true);
    setErrorMsg('');

    CatService.getFeatured()
      .then((response) => setCats(response.data))
      .catch((error) => {
        console.error('Error loading featured cats', error);
        setErrorMsg(
          error?.response?.data?.message || error?.message || 'Unable to load featured cats.'
        );
      })
      .finally(() => setIsLoading(false));
  }, []);

  if (isLoading) {
   return (
    <div className={styles.container}>
      <p className={styles.loading}>
    Loading featured cats...<LoadingSpinner />
    </p>
    </div>
   );
  }

  if (errorMsg) {
    return (
      <div className={styles.container}>
        <h1>Featured Cats</h1>
        <p className={styles.error}>{errorMsg}</p>
      </div>
    );
  }

  return (
    <div className={styles.page}>
    <div className={styles.container}>
      <div className={styles.header}>
        <h1>
          <FontAwesomeIcon icon="crown" className={styles.crownIcon} />
          Featured Cats
        </h1>
        <p className={styles.subtitle}>
          Our top-rated, most exceptional feline professionals
        </p>
      </div>

      {cats.length === 0 ? (
        <div className={styles.empty}>
          <span className={styles.emptyIcon}>
            <FontAwesomeIcon icon="star" />
          </span>
          <p>No featured cats yet.</p>
          <Link to="/cats" className={styles.browseLink}>
            Browse All Cats
          </Link>
        </div>
      ) : (
        <div className={styles.grid}>
          {cats.map((cat) => (
            <div key={cat.catId} className={styles.card}>
              <div className={styles.featuredBadge}>
                <FontAwesomeIcon icon="crown" /> {" "}
                Featured
              </div>

              <Link to={`/cats/${cat.catId}`} className={styles.cardImage}>
                {cat.primaryImagePath ? (
                  <img
                    src={
                      cat.primaryImagePath.startsWith('http')
                        ? cat.primaryImagePath
                        : `http://localhost:9000${cat.primaryImagePath}`
                    }
                    alt={cat.name}
                  />
                ) : (
                  <div className={styles.placeholder}>🐱</div>
                )}
            </Link>


              <div className={styles.cardContent}>
                <h3>
                  <Link to={`/cats/${cat.catId}`}>
                    <FontAwesomeIcon icon="paw" />{" "}{cat.name}</Link>
                </h3>
                
                {cat.rankScore && (
                  <div className={styles.rankScore}>
                    <FontAwesomeIcon icon="star" /> 
                    Rank Score: {cat.rankScore}
                  </div>
                )}
                
                {cat.skills && (
                  <p className={styles.skills}>
                    <strong>Skills:</strong> {cat.skills.substring(0, 100)}
                    {cat.skills.length > 100 && '...'}
                  </p>
                )}
                
                <Link to={`/cats/${cat.catId}`} className={styles.viewBtn}>
                  View Profile
                </Link>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  
  </div>
  );
}
