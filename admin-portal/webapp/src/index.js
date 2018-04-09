import React from 'react'
import ReactDOM from 'react-dom'
import { Provider } from 'react-redux'
import { fromJS } from 'immutable'
import configureStore from './setup/store'
import { BrowserRouter as Router} from 'react-router-dom'
import registerServiceWorker from './registerServiceWorker'
//css
import App from './App'
import Layout from './Layout'

const initialState = fromJS({})
const store = configureStore(initialState)

ReactDOM.render(
    <Provider store={store}>
        <Router>
            <Layout>
                <App/>
            </Layout>
        </Router>
    </Provider>,
    document.getElementById('root'))

registerServiceWorker()
