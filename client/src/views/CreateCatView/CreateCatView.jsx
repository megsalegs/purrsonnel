import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import CatService from '../../services/CatService';
import styles from './CreateCatView.module.css';

export default function CreateCatView() {
  const navigate = useNavigate();

  const [cat, setCat] = useState({
    name: '',
    description: '',
    skills: '',
    featured: false
  });

  const [error, setError] = useState('');

  function handleSubmit(e) {
    e.preventDefault();
    setError('');

    CatService.create(cat)
      .then(res => navigate(`/cats/${res.data.catId}`))
      .catch(() => setError('Failed to create cat'));
  }

  return (
    <div className={styles.page}>
      <div className={styles.container}>
        <h1 className={styles.title}>Create Cat</h1>

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
              value={cat.description}
              onChange={e =>
                setCat({ ...cat, description: e.target.value })
              }
            />
          </div>

          <div className={styles.formGroup}>
            <label>Skills</label>
            <textarea
              value={cat.skills}
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
              Create Cat
            </button>

            <button
              type="button"
              className={styles.cancelBtn}
              onClick={() => navigate('/cats')}
            >
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
