import React, {Component} from 'react'
import { Link, Switch, Redirect, Route, withRouter } from 'react-router-dom'
import NavDashboard from '../../components/NavDashboard'
import routes from './routes'
import Categorys from '../../components/Categorys'

import userPlaceholder from '../../assets/img/userPlaceholder.png'

class Dashboard extends Component {
  render() {
      return (
        <div className='dashboard'>
          <div className='main'>
            <NavDashboard />
            <div className='sidebar'>
              <div className='userInfo flexCenter'>
                <img className='userAvatar' src={userPlaceholder} alt='' />
                <div className='userName'>John Smith</div>
              </div>
              <ul className='nav'>
                <li>
                  <Link href='#mainchart' to='/dashboard/mainchart'>
                    
                  </Link>
                </li>
              </ul>
              <Link className='logout' to='/logout' href='#/logout'>
                <span/>Logout
              </Link>
            </div>
            <div className='d-flex flex-wrap display'>
              <div className='col-md-12 col-lg-6 col-xl-3 categorys'>
                <Categorys
                  category={'Bookings'}
                  value={'184'}
                  icon={'fa fa-couch'}
                  smallIconColor={'red'}
                  smallIcon={'fa fa-exclamation-triangle'}
                  iconDesc={'Get More Space'}
                  iconDescColor={'#b65fc5'}
                  markColor={'#fc960e'}
                />
              </div>
              <div className='col-md-12 col-lg-6 col-xl-3 categorys'>
                <Categorys
                  category={'Website Visits'}
                  value={'75.521'}
                  icon={'fa fa-bar-chart'}
                  smallIcon={'fa fa-tag'}
                  iconDesc={'Tracked from Google Analytics categorys'}
                  iconDescColor={'#cdcdcd'}
                  markColor={'#e22d6c'}
                />
              </div>
              <div className='col-md-12 col-lg-6 col-xl-3 categorys'>
                <Categorys
                  category={'Revenue'}
                  value={'$34,245'}
                  icon={'fa fa-home'}
                  smallIcon={'fa fa-calendar-o'}
                  iconDesc={'Last 24 hours'}
                  iconDescColor={'#cdcdcd'}
                  markColor={'#52ac55'}
                />
              </div>
              <div className='col-md-12 col-lg-6 col-xl-3 categorys'>
                <Categorys
                  category={'Followers'}
                  value={'+245'}
                  icon={'fa fa-twitter'}
                  smallIcon={userPlaceholder}
                  iconDesc={'Just Updated'}
                  iconDescColor={'#cdcdcd'}
                  markColor={'#19bad0'}
                />
              </div>
              {/* <Switch>
                <Route
                  path="/dashboard/mainchart"
                  component={MainChart}
                />
                <Redirect from="*" to="/dashboard/mainchart" />
              </Switch> */}
            </div>
          </div>
        </div>
      )
  }
}

export default withRouter(Dashboard);
