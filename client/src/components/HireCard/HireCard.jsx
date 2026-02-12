// src/components/HireCard/HireCard.jsx
import { Link } from 'react-router-dom';
import styles from './HireCard.module.css';

export default function HireCard({
  hire,
  onCancel,
  onApprove,
  onDeny,
  showCancel,
  showApprove,
  showDeny
}) {
  return (
    <li className={styles.card}>
      <div className={styles.header}>
        <Link to={`/cats/${hire.catId}`} className={styles.imageWrap}>
          {hire.primaryImagePath ? (
            <img
              src={`http://localhost:9000${hire.primaryImagePath}`}
              alt={hire.catName}
              className={styles.image}
            />
          ) : (
            <div className={styles.placeholder}>🐱</div>
          )}
        </Link>

        <div className={styles.meta}>
          <h3>
            <Link to={`/cats/${hire.catId}`}>{hire.catName}</Link>
          </h3>
          <span className={`${styles.status} ${styles[hire.status.toLowerCase()]}`}>
            {hire.status}
          </span>
        </div>
      </div>

      <div className={styles.row}>
        <span className={styles.label}>Dates</span>
        <span>
          {hire.requestedStartDate || '-'} → {hire.requestedEndDate || '-'}
        </span>
      </div>

      <div className={styles.actions}>
        {showCancel && (
          <button className={styles.cancel} onClick={() => onCancel(hire.hireRequestId)}>
            Cancel
          </button>
        )}

        {showApprove && (
          <button className={styles.approve} onClick={() => onApprove(hire.hireRequestId)}>
            Approve
          </button>
        )}

        {showDeny && (
          <button className={styles.deny} onClick={() => onDeny(hire.hireRequestId)}>
            Deny
          </button>
        )}
      </div>
    </li>
  );
}
