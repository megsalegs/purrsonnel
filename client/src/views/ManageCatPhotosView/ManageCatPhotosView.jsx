import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import CatImageService from '../../services/CatImageService';
import LoadingSpinner from '../../components/LoadingSpinner/LoadingSpinner';
import styles from './ManageCatPhotosView.module.css';

export default function ManageCatPhotosView() {
  const { catId } = useParams();
  const [images, setImages] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [errorMsg, setErrorMsg] = useState('');
  const [successMsg, setSuccessMsg] = useState('');

  const [newImage, setNewImage] = useState({
    filePath: '',
    isPrimary: false
  });

  function loadImages() {
    setIsLoading(true);
    setErrorMsg('');

    CatImageService.getByCatId(catId)
      .then(res => setImages(res.data))
      .catch(err => setErrorMsg(err?.response?.data?.message || 'Unable to load photos'))
      .finally(() => setIsLoading(false));
  }

  useEffect(() => {
    loadImages();
  }, [catId]);

function handleAddPhoto(e) {
  e.preventDefault();
  setErrorMsg('');
  setSuccessMsg('');

  console.log('Submitting image:', newImage);

  CatImageService.create(catId, newImage)
    .then(() => {
      setSuccessMsg('Photo added successfully!');
      setNewImage({ filePath: '', isPrimary: false });
      loadImages();
    })
    .catch(err => {
      console.error(err);
      setErrorMsg(
        err?.response?.data?.message || 'Failed to add photo'
      );
    });
}

  function handlePrimary(imageId) {
    if (!window.confirm("Set photo as primary image?")) return;

    CatImageService.setPrimary(catId, imageId)
      .then(() => {
        setSuccessMsg('Photo set!');
        loadImages();
      })
      .catch(err => setErrorMsg('Failed to set photo as primary image'));
  }

  if (isLoading) {
    return (
      <div className={styles.container}>
        <p className={styles.loading}>
          Loading photos... <LoadingSpinner />
        </p>
      </div>
    );
  }

  function handleDelete(imageId) {
    if (!window.confirm('Delete this photo?')) return;

    CatImageService.delete(catId, imageId)
      .then(() => {
        setSuccessMsg('Photo deleted!');
        loadImages();
      })
      .catch(err => setErrorMsg('Failed to delete photo'));
  }

  if (isLoading) {
    return (
      <div className={styles.container}>
        <p className={styles.loading}>
          Loading photos... <LoadingSpinner />
        </p>
      </div>
    );
  }

  return (
    <>
    <div className={styles.page}>
    <div className={styles.container}>
      <Link to={`/cats/${catId}`} className={styles.backLink}>
        ← Back to Cat Profile
      </Link>

      <div className={styles.header}>
   
      <h1>Manage Photos for Cat #{catId}</h1>

      {errorMsg && <p className={styles.error}>{errorMsg}</p>}
      {successMsg && <p className={styles.success}>{successMsg}</p>}
    </div>
    
    <div className={styles.mainContent}>

      <section className={`${styles.section} ${styles.formSection}`}>
        <h2>Add New Photo</h2>
        <form onSubmit={handleAddPhoto} className={styles.form}>
          <div className={styles.formGroup}>
            <label htmlFor="imageUrl">Image URL *</label>
            <input
              type="url"
              id="filePath"
              value={newImage.filePath}
              onChange={e => setNewImage(prev => ({ ...prev, filePath: e.target.value }))}
              placeholder="https://example.com/image.jpg"
              required
            />
          </div>




          
          <button type="submit" className={styles.submitBtn}>
            Add Photo
          </button>
        </form>
      </section>

      <section className={`${styles.section} ${styles.photosSection}`}>
        <h2>Current Photos ({images.length})</h2>

        {images.length === 0 ? (
          <p className={styles.empty}>No photos yet.</p>
        ) : (
          <div className={styles.photoGrid}>
            {images.map(img => (
              
              <div key={img.imageId} className={styles.photoCard}>
                <div className={styles.photoWrapper}>
                  <img src={
                    img.filePath.startsWith('http')
                    ? img.filePath
                    : `http://localhost:9000${img.filePath}`}
                     alt="Cat photo" />
                  {img.isPrimary && (
                    <span className={styles.primaryBadge}>Primary</span>
                  )}
                </div>

              <div className={styles.checkboxGroup}>
                <input
                  type="checkbox"
                  checked={img.isPrimary}
                  onChange={() => handlePrimary(img.imageId)}
                />
                <label>Primary</label>
              </div>


                <button
                  onClick={() => handleDelete(img.imageId)}
                  className={styles.deleteBtn}
                >
                  Delete
                </button>
              </div>
            ))}
          </div>
        )}
      </section>
    </div>
    </div>
  
  </div>
  </>
  );
}