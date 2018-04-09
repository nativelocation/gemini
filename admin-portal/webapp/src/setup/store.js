import { createStore, applyMiddleware, compose } from 'redux'
import createSagaMiddleware from 'redux-saga'

import createReducer from './reducer'
import rootSaga from './sagas'
import Services from './services'

const sagaMiddleware = createSagaMiddleware()

let instance = null

export default function configureStore(initialState = {}, history) {
    const middlewares = [
        sagaMiddleware,
    ]
    const enhancers = [
        applyMiddleware(...middlewares),
    ]

    const composeEnhancers =
        process.env.NODE_ENV !== 'production' &&
        typeof window === 'object' &&
        window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ ?
        window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ : compose

    const store = createStore(
        createReducer(),
        initialState,
        composeEnhancers(...enhancers),
    )

    instance = new Services(store)

    sagaMiddleware.run(rootSaga)
    return store
}

export function services() {
    return instance
}