import { useEffect, useState } from 'react';
import HireRequestService from '../../services/HireRequestService';
import styles from './MyHiresView.module.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export default function MyHiresView() {
  const [hires, setHires] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    HireRequestService.getMine()
      .then(res => setHires(res.data))
      .catch(err =>
        setError(err?.response?.data?.message || 'Unable to load hires.')
      )
      .finally(() => setLoading(false));
  }, []);

  function cancelHire(id) {
    HireRequestService.cancel(id)
      .then(() =>
        setHires(prev =>
          prev.map(h =>
            h.hireRequestId === id ? { ...h, status: 'CANCELLED' } : h
          )
        )
      );
  }

  if (loading) return <p>Loading your hires...</p>;
  if (error) return <p>{error}</p>;
  if (hires.length === 0) return <p>No hire requests yet.</p>;

  return (
    <div className={styles.page}>
      <section className={styles.pawSwat}>
        <div className={styles.pawWrapper}>
            <div className={styles.paw}>
              <img src="/favicon-catPaw.ico" alt="animated paw" />
            </div>
          </div>
        <div className={styles.pawWrapper2}>
            <div className={styles.paw2}>
              <img src="/favicon-catPaw2.ico" alt="animated paw" />
            </div>
        </div>
      </section>
      
    <div className={styles.container}>
      <h1>
          <FontAwesomeIcon icon="shield-cat" className={styles.shieldCat} />
          My Hires
        </h1>

      <ul className={styles.list}>
        {hires.map(h => (
          <li key={h.hireRequestId} className={styles.card}>
            {h.primaryImagePath ? (
              <img
                src={
                  h.primaryImagePath?.startsWith('http')
                  ? h.primaryImagePath
                  : `http://localhost:9000${h.primaryImagePath}`
                }
                alt={h.catName}
                className={styles.image}
              />
            ) : (
              <div className={styles.placeholder}>🐱</div>
            )}
            
            <h3>{h.catName}</h3>

            <div className={styles.statusBadge}>
                Status: {h.status}
            </div>
          

            {h.requestedStartDate && (
              <p>
                {h.requestedStartDate} → {h.requestedEndDate}
              </p>
            )}

            {h.status === 'PENDING' && (
              <button
              className={styles.cancelBtn}
              onClick={() => cancelHire(h.hireRequestId)}>
                Cancel Request
              </button>
            )}
          </li>
        ))}
      </ul>
    </div>
  
    </div>
    
  );
}
