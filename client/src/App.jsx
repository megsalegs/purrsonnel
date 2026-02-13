import { Navigate, Routes, Route } from 'react-router-dom';

import NavBar from './components/NavBar/NavBar';
import ProtectedRoute from './components/ProtectedRoute';

import CatsView from './views/CatsView/CatsView';
import CatDetailsView from './views/CatDetailsView/CatDetailsView';
import FeaturedCatsView from './views/FeaturedCatsView/FeaturedCatsView';

import LoginView from './views/LoginView/LoginView';
import LogoutView from './views/LogoutView';
import RegisterView from './views/RegisterView/RegisterView';

import BookmarksView from './views/BookmarksView/BookmarksView';
import HireRequestsView from './views/HireRequestsView/HireRequestsView';

import ManageCatPhotosView from './views/ManageCatPhotosView/ManageCatPhotosView';
import ManageReviewsView from './views/ManageReviewsView/ManageReviewsView';
import LandingPage from './views/LandingPage/LandingPage';
import MyHiresView from './views/MyHiresView/MyHiresView';
import CreateCatView from './views/CreateCatView/CreateCatView';
import EditCatView from './views/EditCatView/EditCatView';
import Footer from './components/Footer/Footer';



export default function App() {

  return (
    <div id="purrsonnel-app">
        <NavBar />
        <main>
          <Routes>
             
            <Route path="/" element={<LandingPage />} />
            <Route path="/cats" element={<CatsView />} />
            <Route path="/cats/featured" element={<FeaturedCatsView />} />
            <Route path="/cats/:catId" element={<CatDetailsView />} />
            
            <Route path="/login" element={<LoginView />} />
            <Route path="/logout" element={<LogoutView />} />
            <Route path="/register" element={<RegisterView />} />
            
            <Route
            path="/bookmarks"
            element={
              <ProtectedRoute>
                <BookmarksView />
              </ProtectedRoute>
            }
           />

            <Route
              path="/hire-requests/mine"
              element={
                <ProtectedRoute>
                  <MyHiresView />
                </ProtectedRoute>
              }
            />

            <Route
            path="/hire-requests"
            element={
              <ProtectedRoute>
                <HireRequestsView />
              </ProtectedRoute>
            }
           />
           
                     <Route
            path="/admin/cats/:catId/photos"
            element={
              <ProtectedRoute>
                <ManageCatPhotosView />
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin/reviews"
            element={
              <ProtectedRoute roles={['ROLE_STAFF']}>
                <ManageReviewsView />
              </ProtectedRoute>
            }
          />
      
          <Route 
            path="/admin/cats/create"
            element={
            <ProtectedRoute roles={['ROLE_STAFF']}>
              <CreateCatView />
            </ProtectedRoute>
            }
          />

          <Route 
            path="/admin/cats/:catId/edit"
            element={
            <ProtectedRoute roles={['ROLE_STAFF']}>
              <EditCatView />
              </ProtectedRoute>
              }
          />


          
          <Route path="*" element={<Navigate to="/cats" replace />} />
        </Routes>
        </main>
        <Footer />
    </div>
  );
}

     

          
