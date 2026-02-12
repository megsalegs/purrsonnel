import styles from './CatDetailsSection.module.css';

export default function CatDetailsSection({ cat }) {
  if (!cat) return null;

	return (

<div className={styles.detailsSection}>
        <ul className={styles.detailsList}>
        <li>
          <strong>Age: </strong> {cat.age}
        </li>
        <li>
          <strong>Color: </strong> {cat.color}
        </li>
        <li>
          <strong>Fur Length: </strong> {cat.fur_length}
        </li>
        </ul>

        {cat.description && (
          <p className={styles.description}>
            {cat.description}
          </p>
        )}

        
          <div className={styles.skills}>
          <strong>Skills: </strong>
          {cat.skills}
          </div>
        
      </div>
	  
	);
}