/**
 * Created by fran on 1/26/18.
 */

import React, {Component} from "react";
import DateInput from "../../../components/DateInput";
import TextInput from "../../../components/TextInput";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import {searchStudent} from "../../../redux/actions";
import Immutable from "immutable";

class StudentIdentification extends Component {

    constructor(props) {
        super(props);
        this.inputHandler = this.inputHandler.bind(this);
        this.onValidDate = this.onValidDate.bind(this);
    }

    inputHandler(e) {
        let form = this.props.form;
        let element = e.target;
        form[element.id] = element.value;
    }

    onValidDate(date) {
        let form = this.props.form;
        form.dob = date;
    }

    onPress(onResult, onError) {
        let form = this.props.form;
        this.props.searchStudent(form, onResult, onError);
    }

    render() {
        let form = this.props.form.toJS();
        return (<div className="row">
            <div className="col-md-4">
                <TextInput id="lastSSN"
                           type="lastSSN"
                           value={form.lastSSN}
                           onChange={this.inputHandler}
                           placeholder="Ultimo 4 digitos seguro social"/>
            </div>
            <div className="col-md-4">
                <TextInput id="studentNumber"
                           type="studentNumber"
                           value={form.studentNumber}
                           onChange={this.inputHandler}
                           placeholder="Numero de Estudiante SIE"/>
            </div>

            <div className="col-md-4">
                <DateInput id="dob"
                           value={form.dob}
                           onValidDate={this.onValidDate}
                           label="Fecha nacimiento"/>
            </div>
        </div>);
    }
}

function mapStateToProps(store) {
    return {form: Immutable.fromJS(store.studentLookup.form)};
}

function mapDispatchToActions(dispatch) {
    return bindActionCreators({searchStudent}, dispatch)
}

export default connect(mapStateToProps, mapDispatchToActions, null, {withRef: true})(StudentIdentification);
