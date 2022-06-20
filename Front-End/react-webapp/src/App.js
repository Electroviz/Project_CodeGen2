import logo from './logo.svg';
import './App.css';
import { BrowserRouter as Router, Route,Routes, Link} from 'react-router-dom';

import LoginComp from './components/loginComponent';
import BankingPage from './components/bankingPageComponent';

function App() {
  return (
    <Router>
      <div className="App">
        {/* <LoginComp /> */}
        <Routes>
          <Route path="/homePage" element={ BankingPage() } />
        </Routes>

        <Routes>
          <Route path="/" element={ LoginComp() } />
        </Routes>

        
      </div>
    </Router>
  );
}

export default App;
