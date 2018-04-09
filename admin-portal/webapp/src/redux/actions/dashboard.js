import * as constants from '../constants'

export const dashboard = () => (dispatch) => {
    dispatch({
        type: constants.DASHBOARD_START
    })
};

