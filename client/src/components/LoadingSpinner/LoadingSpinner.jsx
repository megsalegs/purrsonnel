import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faRotate } from '@fortawesome/free-solid-svg-icons';

import styles from './LoadingSpinner.module.css';

export default function LoadingSpinner() {
  return (
    <FontAwesomeIcon
      icon={faRotate}
      className={`${styles.loadingSpinner} fa-spin`}
    />
  );
}
