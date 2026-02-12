import { useParams, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import CatService from '../../services/CatService';
import styles from './EditCatView.module.css';

export default function EditCatView() {
  const { catId } = useParams();
  const navigate = useNavigate();
  const [cat, setCat] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => {
    CatService.getById(catId)
      .then(res => setCat(res.data))
      .catch(() => setError('Unable to load cat'));
  }, [catId]);

  function handleSubmit(e) {
    e.preventDefault();
    setError('');

    CatService.update(catId, cat)
      .then(() => navigate(`/cats/${catId}`))
      .catch(() => setError('Update failed'));
  }

  function handleDelete() {
    if (!window.confirm('Delete this cat?')) return;

    CatService.remove(catId)
      .then(() => navigate('/cats'))
      .catch(() => setError('Delete failed'));
  }

  if (!cat) return <p>Loading...</p>;

  return (
    <div className={styles.page}>
      <div className={styles.container}>
        <h1 className={styles.title}>Edit Cat</h1>

        {error && <p className={styles.error}>{error}</p>}

        <form onSubmit={handleSubmit} className={styles.form}>
          <div className={styles.formGroup}>
            <label>Name</label>
            <input
              value={cat.name}
              onChange={e => setCat({ ...cat, name: e.target.value })}
              required
            />
          </div>

          <div className={styles.formGroup}>
            <label>Description</label>
            <textarea
              value={cat.description || ''}
              onChange={e =>
                setCat({ ...cat, description: e.target.value })
              }
            />
          </div>

          <div className={styles.formGroup}>
            <label>Skills</label>
            <textarea
              value={cat.skills || ''}
              onChange={e =>
                setCat({ ...cat, skills: e.target.value })
              }
            />
          </div>

          <div className={styles.checkboxGroup}>
            <input
              type="checkbox"
              checked={cat.featured}
              onChange={e =>
                setCat({ ...cat, featured: e.target.checked })
              }
            />
            <label>Featured Cat</label>
          </div>

          <div className={styles.actions}>
            <button type="submit" className={styles.submitBtn}>
              Save Changes
            </button>

            <button
              type="button"
              className={styles.cancelBtn}
              onClick={() => navigate(`/cats/${catId}`)}
            >
              Cancel
            </button>
          </div>
        </form>

        <div className={styles.deleteSection}>
          <button
            className={styles.deleteBtn}
            onClick={handleDelete}
          >
            Delete Cat
          </button>
        </div>
      </div>
    </div>
  );
}
