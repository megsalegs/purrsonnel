import { NavLink } from "react-router-dom";
import styles from './CatPhotoSection.module.css';
import CatDetailsSection from "../CatDetailsSection/CatDetailsSection";

export default function CatPhotosSection({ images, loading, catName, catId, isStaff, cat }) {
	return (
		<section className={styles.topGrid}>
			<div>
			<h2>Photos</h2>
     		{loading && <p>Loading photos...</p>}
			{!loading && images.length === 0 && <p>No photos yet.</p>}
    		<div className={styles.photos}>
        		{images.map(image => (
          			<img
            			key={image.imageId}
            			src={`http://localhost:9000${image.filePath}`}
            			alt={catName}
            			className={styles.photo}
          			/>
        		))}
      		</div>

      		{isStaff && (
        	<div className={styles.adminActions}>
          		<NavLink 
            		to={`/admin/cats/${catId}/photos`} 
           			className={styles.managePhotosBtn}
          		>
          		📸 Manage Photos
          		</NavLink>
          	</div>
      		)}
			</div>
			<CatDetailsSection cat={cat} />
	  		</section>
	);
}