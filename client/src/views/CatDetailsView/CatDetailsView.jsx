import { useContext, useEffect, useState } from 'react';
import { Link, NavLink, useParams, useNavigate } from 'react-router-dom';
import CatService from '../../services/CatService';
import CatImageService from '../../services/CatImageService';
import BookmarkService from '../../services/BookmarkService';
import HireRequestService from '../../services/HireRequestService';
import ReviewService from '../../services/ReviewService';
import { UserContext } from '../../context/UserContext';
import styles from './CatDetailsView.module.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { motion, AnimatePresence } from 'framer-motion';
import HireFeedbackService from '../../services/HireFeedbackService';

export default function CatDetailsView() {
  const { catId } = useParams();
  const { user } = useContext(UserContext);
  const navigate = useNavigate();

  const [cat, setCat] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [errorMsg, setErrorMsg] = useState('');

  const [images, setImages] = useState([]);
  const [imagesLoading, setImagesLoading] = useState(true);
  const [imagesError, setImagesError] = useState('');

  const [isBookmarked, setIsBookmarked] = useState(false);
  const [bookmarkBusy, setBookmarkBusy] = useState(false);
  const [bookmarkMsg, setBookmarkMsg] = useState('');

  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [hireMsg, setHireMsg] = useState('');
  const [hireErr, setHireErr] = useState('');
  const [hireSubmitting, setHireSubmitting] = useState(false);

  const [reviews, setReviews] = useState([]);
  const [reviewsLoading, setReviewsLoading] = useState(true);
  const [reviewsError, setReviewsError] = useState('');

  const [reviewBody, setReviewBody] = useState('');
  const [rating, setRating] = useState('5');
  const [reviewSubmitting, setReviewSubmitting] = useState(false);
  const [reviewMsg, setReviewMsg] = useState('');
  const [reviewErr, setReviewErr] = useState('');

  const [showReviews, setShowReviews] = useState(false);

  const [feedbackSubmitting, setFeedbackSubmitting] = useState(false);
  const [feedbackMsg, setFeedbackMsg] = useState('');
  const [feedbackErr, setFeedbackErr] = useState('');
  const [eligibleHireRequestId, setEligibleHireRequestId] = useState(null);


  const PREVIEW_COUNT = 2;
  const hiddenCount = Math.max(0, reviews.length - PREVIEW_COUNT);
  const visibleReviews = showReviews
    ? reviews : reviews.slice(0, PREVIEW_COUNT);

  const reviewItemVariants = {
    hidden: { opacity: 0, y: -8 },
    visible: { opacity: 1, y: 0 }
  };








  const isStaff = user?.role === 'ROLE_STAFF';
  useEffect(() => {
    setIsLoading(true);
    setErrorMsg('');

    CatService.getById(catId)
      .then(res => setCat(res.data))
      .catch(err =>
        setErrorMsg(err?.response?.data?.message || 'Unable to load cat.')
      )
      .finally(() => setIsLoading(false));
  }, [catId]);

  useEffect(() => {
    setImagesLoading(true);
    setImagesError('');

    CatImageService.getByCatId(catId)
      .then(res => setImages(res.data))
      .catch(err =>
        setImagesError(err?.response?.data?.message || 'Unable to load photos.')
      )
      .finally(() => setImagesLoading(false));
  }, [catId]);

  useEffect(() => {
    setReviewsLoading(true);
    setReviewsError('');

    ReviewService.getByCatId(catId)
      .then(res => setReviews(res.data))
      .catch(err =>
        setReviewsError(err?.response?.data?.message || 'Unable to load reviews.')
      )
      .finally(() => setReviewsLoading(false));
  }, [catId]);

  function submitHireRequest() {
    setHireMsg('');
    setHireErr('');
    setHireSubmitting(true);

    HireRequestService.create({
      catId: cat.catId,
      requestedStartDate: startDate || null,
      requestedEndDate: endDate || null
    })
      .then(() => {
        setHireMsg('Hire request submitted.');
        setStartDate('');
        setEndDate('');
      })
      .catch(err =>
        setHireErr(err?.response?.data?.message || 'Unable to submit hire request.')
      )
      .finally(() => setHireSubmitting(false));
  }

  function submitReview(e) {
    e.preventDefault();
    console.log("SUBMIT FIRED");
    setReviewMsg('');
    setReviewErr('');
    setReviewSubmitting(true);

    ReviewService.create(cat.catId, {
      reviewBody: reviewBody.trim(),
      rating: Number(rating)
    })
      .then(() => {
        setReviewMsg('Review added!');
        setReviewBody('');
        
        setTimeout(() => {
          ReviewService.getByCatId(cat.catId)
          .then(res => {
            setReviews(res.data);
            setShowReviews(true);
            setReviewMsg('');
          });
      }, 2000);
      })
      .catch(err =>
      setReviewErr(
        err?.response?.data?.message || 'Unable to create review.')
      )
      .finally(() => setReviewSubmitting(false));
  }

useEffect(() => {
  if (!user || !cat?.catId) return;

  HireRequestService.getMine()
    .then(res => {
      console.log("Hire requests:", res.data);
      console.log("Current catId:", cat.catId);

      const match = res.data.find(h =>
        h.catId === cat.catId &&
        h.status === "APPROVED"
      );

      console.log("Matched hire:", match);

      if (match) {
        setEligibleHireRequestId(match.hireRequestId);
      }
    });
}, [user, cat]);



  function submitHireFeedback(hireRequestId) {
    setFeedbackMsg('');
    setFeedbackErr('');
    setFeedbackSubmitting(true);

    HireFeedbackService.create(hireRequestId, {
      rating: Number(rating)
    })
    .then (() => {
      setFeedbackMsg('Rating submitted.');
      setRating('5');
    })
    .catch(err =>
    setFeedbackErr(
      err?.response?.data?.message || 'Unable to submit rating.'
    ))
    .finally(() => setFeedbackSubmitting(false));
    
  }

  useEffect(() => {
    if (!user || !cat?.catId) return;

    BookmarkService.getMine()
      .then(res =>
        setIsBookmarked(res.data.some(c => c.catId === cat.catId))
      )
      .catch(() => setIsBookmarked(false));
  }, [user, cat]);

  function toggleBookmark() {
    setBookmarkBusy(true);
    const action = isBookmarked ? BookmarkService.remove : BookmarkService.add;

    action(cat.catId)
      .then(() => {
        setIsBookmarked(prev => !prev);
        setBookmarkMsg(isBookmarked ? 'Bookmark removed.' : 'Bookmarked.');
      })
      .finally(() => setBookmarkBusy(false));
  }

  function toggleFeatured() {
  CatService.update(cat.catId, {
    ...cat,
    featured: !cat.featured
  })
    .then(res => setCat(res.data))
    .catch(() => alert('Failed to update featured status'));
}

function handleDelete() {
  if (!window.confirm('Delete this cat?')) return;

  CatService.remove(cat.catId)
    .then(() => navigate('/cats'))
    .catch(() => alert('Delete failed'));
}



  if (isLoading) return <p>Loading cat...</p>;
  if (errorMsg) return <p>{errorMsg}</p>;
  if (!cat) return <p>Cat not found.</p>;

  return (
    <>
    <div className={styles.page}>
    <div className={styles.container}>
      <NavLink to="/cats" className={styles.backLink}>← Back</NavLink>

      <div className={styles.header}>
      <h1>
        <FontAwesomeIcon icon="paw" className={styles.pawIcon1} />
        {cat.name}
        <FontAwesomeIcon icon="paw" className={styles.pawIcon2} />
      </h1>
    
      
      </div>

      <div className={styles.mainContent}>
      

      <section className={`${styles.section} ${styles.photoSct}`}>
        {imagesLoading && <p>Loading photos...</p>}
        {imagesError && <p className={styles.error}>{imagesError}</p>}
        {!imagesLoading && images.length === 0 && <p>No photos yet.</p>}
          <div className={styles.photos}>
              {images.map(image => (
                  <img
                    key={image.imageId}
                    src={
                      image.filePath.startsWith('http')
                        ? image.filePath
                        : `http://localhost:9000${image.filePath}`
                    }
                    alt={cat.name}
                    className={styles.photo}
                  />
              ))}
              <div className={styles.adminActions}>
              {isStaff && (
          		<NavLink 
            		to={`/admin/cats/${catId}/photos`} 
           			className={styles.managePhotosBtn}
          		>
          		<FontAwesomeIcon icon="camera" /> 
              {" "}{" "}
              Manage Photos
          		</NavLink>
      		)}
          </div>
          </div>
      </section>

     {user && (
        <>
          <button
            onClick={toggleBookmark}
            disabled={bookmarkBusy}
            className={styles.bookmarkBtn}
          >
            {isBookmarked ? '★ Bookmarked' : '☆ Bookmark'}
          </button>

          {bookmarkMsg && (
            <p className={styles.success}>{bookmarkMsg}</p>
          )}
        </>
      )}


      <section className={`${styles.section} ${styles.details}`}>
        <h2>Details</h2>
      {cat.description && (
          <div className={styles.description}>
            <p>{cat.description}</p>
          </div>
        )}
      

    
      
      
        
        <ul className={styles.detailsList}>
          <li>
          <strong>Skills:  </strong> {cat.skills}
        </li>
        <li>
          <strong>Average Rating: </strong>
          {cat.rankScore ?? 'Not yet rated'}
        </li>
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
        </section>
        
      

     <section className={`${styles.section} ${styles.reviewSct}`}>
      <div className={styles.reviewHeaderRow}>
        <h2>Reviews</h2>

        {hiddenCount > 0 && (
          <button
            type="button"
            onClick={() => setShowReviews(v => !v)}
            className={styles.reviewToggle}
          >
            {showReviews ? 'Hide' : `+${hiddenCount} more`}
          </button>
        )}
      </div>

        {reviewsLoading && <p>Loading reviews...</p>}
        {reviewsError && <p className={styles.error}>{reviewsError}</p>}

      <motion.ul
        className={styles.reviewList}
        layout
      >
        <AnimatePresence initial={false}>
          {visibleReviews.map(r => (
            <motion.li
              key={r.reviewId}
              layout
              variants={reviewItemVariants}
              initial="hidden"
              animate="visible"
              exit="hidden"
              transition={{ duration: 0.25, ease: 'easeOut' }}
              className={styles.reviewCard}
            >
              <div className={styles.reviewHeader}>
                <span className={styles.reviewRating}>{r.rating}/5</span>
              </div>
              <p className={styles.reviewBody}>{r.reviewBody}</p>
            </motion.li>
          ))}
        </AnimatePresence>
      </motion.ul>
    </section>



{user && (
        <section className={styles.hireSection}>
          <h2>Request Hire</h2>
          <form className={styles.hireForm}>
            <label>
              Start Date
                <input type="date" value={startDate} onChange={e => setStartDate(e.target.value)} />
            </label>
            <label>
              End Date
                <input type="date" value={endDate} onChange={e => setEndDate(e.target.value)} />
            </label>
          <button onClick={submitHireRequest} disabled={hireSubmitting}>Submit</button>
          </form>
          {hireMsg && <p>{hireMsg}</p>}
          {hireErr && <p className={styles.error}>{hireErr}</p>}
        </section>
      )}

    {user && eligibleHireRequestId && (
      <section className={styles.hireFeedbackSection}>
        <h3>Rate This Hire</h3>

        <select
          value={rating}
          onChange={e => setRating(e.target.value)}
          required
        >
          {[1, 2, 3, 4, 5].map(n => (
            <option key={n} value={n}>
              {n}
            </option>
          ))}
        </select>

        <button
          type="button"
          onClick={() => submitHireFeedback(eligibleHireRequestId)}
          disabled={feedbackSubmitting}
        >
          Submit Rating
        </button>

        {feedbackMsg && <p className={styles.success}>{feedbackMsg}</p>}
        {feedbackErr && <p className={styles.error}>{feedbackErr}</p>}
      </section>
    )}


{user && (
 <div className={styles.addRev}>
        <form onSubmit={submitReview} className={styles.reviewForm}>
          <h3>Add Review</h3>
          <textarea
            value={reviewBody}
            onChange={e => setReviewBody(e.target.value)}
            maxLength={500}
            required
          />
        
          <label>
            Rating {" "}
            <select
              value={rating}
              onChange={e => setRating(e.target.value)}
              required
            >
              {[1,2,3,4,5].map(n => (
                <option key={n} value={n}>
                  {n}
                </option>
              ))}
            </select>
          </label>
      
          <button type="submit" disabled={reviewSubmitting}>Submit</button>
          {reviewMsg && (
            <p className={styles.success}>
              {reviewMsg}
            </p>
          )}
          {reviewErr && <p className={styles.error}>{reviewErr}</p>}
        </form>
      </div>
 )}

      {isStaff && (
  <div className={styles.staffActions}>
    <button
      onClick={toggleFeatured}
      className={`${styles.actionBtn} ${styles.featureBtn}`}
    >
      {cat.featured ? 'Remove Featured' : 'Mark as Featured'}
    </button>

    <Link
      to={`/admin/cats/${catId}/edit`}
      className={`${styles.actionBtn} ${styles.editBtn}`}
    >
       Edit Cat
    </Link>

    <button
      onClick={handleDelete}
      className={`${styles.actionBtn} ${styles.deleteBtn}`}
    >
      🗑 Delete Cat
    </button>
  </div>
)}




    </div>


     

    

   

      
    </div>
  </div>

      <footer className={styles.footer}>
				&copy; Purrsonnel
			</footer>
    
  </>
);
}