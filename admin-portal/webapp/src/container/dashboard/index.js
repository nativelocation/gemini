import React, {Component} from 'react'
import { Link, Switch, Redirect, Route, withRouter } from 'react-router-dom'
import NavDashboard from '../../components/NavDashboard'
import routes from './routes'
import Categorys from '../../components/Categorys'
import { Line as LineChart } from 'react-chartjs'

import userPlaceholder from '../../assets/img/userPlaceholder.png'

class Dashboard extends Component {
  state ={
    chartData: {}
  }

  componentWillMount() {
    this.setState({
      chartData: {
        labels: ["January", "February", "March", "April", "May", "June", "July"],
        datasets: [
          {
            label: "My First dataset",
            fillColor: "rgba(220,220,220,0.2)",
            strokeColor: "rgba(220,220,220,1)",
            pointColor: "rgba(220,220,220,1)",
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(220,220,220,1)",
            data: [65, 59, 80, 81, 56, 55, 40]
          },
          {
            label: "My Second dataset",
            fillColor: "rgba(151,187,205,0.2)",
            strokeColor: "rgba(151,187,205,1)",
            pointColor: "rgba(151,187,205,1)",
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(151,187,205,1)",
            data: [28, 48, 40, 19, 86, 27, 90]
          }
        ],
      }
    })
  }
  render() {
    const { chartData } = this.state
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
          <div className='display'>
            <div className='d-flex flex-wrap'>
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
            </div>
            <div className='dashboard-chart'>
              <LineChart data={chartData} options={null} width="600" height="250"/>
            </div>
            
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
    )
  }
}

export default withRouter(Dashboard);
