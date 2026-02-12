import { useEffect, useState, useContext } from 'react';
import { UserContext } from '../../context/UserContext';
import { Link } from 'react-router-dom';
import HireRequestService from '../../services/HireRequestService';
import styles from './HireRequestsView.module.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export default function HireRequestsView() {
  const { user } = useContext(UserContext);
  const [hires, setHires] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');

useEffect(() => {
  if (user) {
    loadHires();
  }
}, [user]);


  function loadHires() {
    setIsLoading(true);
    setError('');

    const request =
      user?.role === 'ROLE_STAFF'
        ? HireRequestService.getAll()
        : HireRequestService.getMine();

    request
      .then(res => setHires(res.data))
      .catch(err =>
        setError(err?.response?.data?.message || 'Unable to load hire requests.')
      )
      .finally(() => setIsLoading(false));

  }

function approveHire(hireRequestId) {
  HireRequestService.updateStatus(hireRequestId, 'APPROVED')
    .then(loadHires)
    .catch(err =>
      setError(err?.response?.data?.message || 'Unable to approve request.')
    );
}


function denyHire(hireRequestId) {
  HireRequestService.updateStatus(hireRequestId, 'REJECTED')
    .then(loadHires)
    .catch(err =>
      setError(err?.response?.data?.message || 'Unable to deny request.')
    );
}



  function formateDate(dateString) {
    if (!dateString) return '-';

    return new Date(dateString).toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric'
    });
  }

  if (isLoading) {
    return <p className={styles.status}>Loading your hires…</p>;
  }

  return (
    <div className={styles.page}>
      <div className={styles.container}>
        <h1>
          <FontAwesomeIcon icon="shield-cat" className={styles.shieldCat} />
          Hire Requests
        </h1>
        

        {error && <p className={styles.error}>{error}</p>}

        {hires.length === 0 ? (
          <p className={styles.empty}>You haven’t requested any hires yet.</p>
        ) : (
          <ul className={styles.list}>
            {hires.map(h => (
              <li key={h.hireRequestId} className={styles.card}>

              
                <div className={styles.header}>
                  <Link to={`/cats/${h.catId}`} className={styles.imageWrap}>
                    {h.primaryImagePath ? (
                      <img
                      className={styles.image}
                        src={
                          h.primaryImagePath.startsWith('http')
                            ? h.primaryImagePath
                            : `http://localhost:9000${h.primaryImagePath}`
                        }
                        alt={h.catName}
                      />
                    ) : (
                      <div className={styles.placeholder}>🐱</div>
                    )}

                  </Link>


                  <div className={styles.meta}>
                    <h3>
                      <Link to={`/cats/${h.catId}`}>{h.catName}</Link>
                    </h3>
                    <span className={`${styles.statusBadge} ${styles[h.status?.toLowerCase()]}`}>
                      {h.status}
                    </span>
                  </div>
                </div>

              
                <div className={styles.row}>
                  <span className={styles.label}>Dates: {" "}
                  
                    {formateDate(h.requestedStartDate)} → {formateDate(h.requestedEndDate)}
                  </span>
                </div>

             
                {h.status === 'PENDING' && (
                 <div className={styles.actions}>
                  <button
                    className={`${styles.button} ${styles.approve}`}
                    onClick={() => approveHire(h.hireRequestId)}
                  >
                    Approve
                  </button>

                  <button
                    className={`${styles.button} ${styles.deny}`}
                    onClick={() => denyHire(h.hireRequestId)}
                  >
                    Deny
                  </button>
                 </div>
                )}

              </li>
            ))}
          </ul>
        )}
      </div>

      <footer className={styles.footer}>
        &copy; Purrsonnel
      </footer>
    </div>
  );
}

