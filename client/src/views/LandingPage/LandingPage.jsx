import { Link, NavLink } from 'react-router-dom';
import styles from './LandingPage.module.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { UserContext } from '../../context/UserContext';
import { useContext } from 'react';

export default function LandingPage() {
	const { user } = useContext(UserContext);
  	const isLoggedIn = !!user?.token;

	return (
		<div className={styles.landingPage}>
			<div className={styles.pawWrapper}>
			<div className={styles.paw}>
				<img src="/favicon-catPaw.ico" alt="animated paw" />
			</div>
			</div>
			
			

			<section className={styles.hero}>
				<div className={styles.heroContent}>
					<div className={styles.logoContainer}>
						<span className={styles.pawIcon}> </span>
						<h1 className={styles.mainTitle}>
							Purr<span className={styles.highlight}>sonnel</span>
						</h1>
						<span className={styles.pawIcon}>
							<FontAwesomeIcon icon="paw" />
						</span>
					</div>

					<p className={styles.tagLine}>
						Find Your Purr-fect Hire
					</p>

					<p className={styles.subtitle}>
						The premier employment agency connecting employers with exceptional cats.
					</p>

					<div className={styles.ctaButtons}>
						<Link to="/register" className={styles.secondaryBtn}>
							Join the Team
						</Link>
					</div>
				</div>

				<div className={styles.heroImage}>
					<div className={styles.floatingCat}>
						<img src="/favicon-cat1.ico" alt="Floating cat icon" />
					</div>
					<div className={styles.floatingCat}>
						<img src="/favicon-cat3.ico" alt="Floating cat icon" />
					</div>
					<div className={styles.floatingCat}>
						<img src="/favicon-cat2.ico" alt="Floating cat icon" />
					</div>
					<div className={styles.floatingCat}>
						<img src="/favicon-cat4.ico" alt="Floating cat icon" />
					</div>
				</div>
			</section>

			<section className={styles.features}>
				<h2 className={styles.sectionTitle}>Why choose Purrsonnel?</h2>

				<div className={styles.featureGrid}>
					<div className={styles.featureCard}>
						<div className={styles.featureIcon}>
							<FontAwesomeIcon icon="cat" />
						</div>
						<h3>Professional Felines</h3>
						<p>
							Our cats are highly skilled, well-trained, and ready to excel in roles from mousing
							to software development.
						</p>
					</div>

					<div className={styles.featureCard}>
						<div className={styles.featureIcon}>
							<FontAwesomeIcon icon="star" />
						</div>
						<h3>Featured Talent</h3>
						<p>
							Browse our featured cats - top performers with proven track records and stellar reviews
							from satisfied employers.
						</p>
					</div>

					<div className={styles.featureCard}>
						<div className={styles.featureIcon}>
							<FontAwesomeIcon icon={["far", "bookmark"]} />
						</div>
						<h3>Easy Bookmarking</h3>
						<p>
							Create an account to bookmark your favorite cats and keep track of potential hires for 
							future projects.
						</p>
					</div>

					<div className={styles.featureCard}>
						<div className={styles.featureIcon}>
							<FontAwesomeIcon icon={["far", "calendar"]} />
						</div>
						<h3>Simple Hiring</h3>
						<p>
							Submit hire requests with just a few clicks. Specify dates, review applications, and 
							get your purr-fect match.
						</p>
					</div>

					<div className={styles.featureCard}>
						<div className={styles.featureIcon}>
							<FontAwesomeIcon icon="star" />
						</div>
						<h3>Verified Reviews</h3>
						<p>
							Read authentic reviews from real employers. Every cat's performance is rated and documented
							for transparency.
						</p>
					</div>

					<div className={styles.featureCard}>
						<div className={styles.featureIcon}>
							<FontAwesomeIcon icon="trophy" />
						</div>
						<h3>Ranked by Excellence</h3>
						<p>
							Our ranking system helps you find the best talent. Cats are scored based on skills, reviews,
							and successful projects.
						</p>
					</div>
				</div>
			</section>

			<section className={styles.finalCta}>
				<h2>Ready to Hire Your Purr-fect Employee?</h2>
				<p>Your dream team starts at Purrsonnel.</p>

				<div className={styles.ctaButtons}>
					<Link to="/cats" className={styles.primaryBtn}>
						Explore Talent Now
					</Link>
					{isLoggedIn && <NavLink to="/login" className={styles.secondaryBtn}>
						Sign In
					</NavLink>}
					{!isLoggedIn && <NavLink to="/login" className={styles.secondaryBtn1}>
						Sign In
					</NavLink>}
				</div>

				<div className={styles.catEmojis}>
						<img src="/favicon-cat1.ico" alt="Floating cat icon" />
						<img src="/favicon-cat2.ico" alt="Floating cat icon" />
						<img src="/favicon-cat3.ico" alt="Floating cat icon" />
						<img src="/favicon-cat4.ico" alt="Floating cat icon" />
				</div>
			</section>

			<footer className={styles.footer}>
				&copy; Purrsonnel
			</footer>
		</div>
	);
}