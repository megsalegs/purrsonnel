import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCrown } from '@fortawesome/free-solid-svg-icons';

import styles from './FeaturedIcon.module.css';

export default function FeaturedIcon() {
  return (
    <FontAwesomeIcon
      icon={faCrown}
      className={`${styles.featuredIcon} fa-spin`}
    />
  );
}
