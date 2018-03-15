/**
 * Created by fran on 2/2/18.
 */
import React, {Component} from "react";
import {Button, Glyphicon} from "react-bootstrap";
import moment from "moment";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import {loadHome, resetWizard} from "../redux/actions";
import * as Utils from "../Utils";

class Home extends Component {

    constructor(props) {
        super(props);
        this.preEnroll = this.preEnroll.bind(this);
    }

    componentWillMount() {
        this.props.resetWizard();
        this.props.loadHome();
    }

    preEnroll() {
        this.props.history.push("/wizard");
    }

    editPreEnroll = id => e => {
        this.props.history.push(`/wizard/${id}`);
    };

    render() {
        return (
            <div className="container">
                <div style={{marginTop: 20}}>
                    <div className="row">
                        <div className="col-md-10">
                            <h3 style={{textAlign: "right"}}>Desea pre-matricular un estudiante?</h3>
                        </div>
                        <div className="col-md-2" style={{marginTop: 20}}>
                            <Button onClick={this.preEnroll} bsStyle="primary" block={true}>Pre-Matricular</Button>
                        </div>
                    </div>
                    <div className="row" style={{marginTop: 20}}>
                        <div className="col-md-12">
                            {this.renderPreEnrollmentList()}
                        </div>
                    </div>
                </div>
            </div>
        );
    }

    renderPreEnrollmentList() {
        let preEnrollments = this.props.preEnrollments;
        if (!preEnrollments || preEnrollments.length <= 0)
            return (
                <div className="panel panel-default">
                    <div className="panel-body">
                        No posee pre-matriculas aun
                    </div>
                </div>
            );

        return preEnrollments.map((pre, index) => (
            <div key={index} className="panel panel-primary" style={{height: 150}}>
                <div className="panel-heading">
                    Estudiante {pre.student.fullName} -> {Utils.format(pre.student.dateOfBirth, "ll")}
                    <div className="pull-right" style={{marginTop: -5}}>
                        {pre.requestStatus === "ACTIVE"
                            ?
                            (<Button bsSize="small" bsStyle="info" onClick={this.editPreEnroll(pre.id)}>
                                <Glyphicon glyph="glyphicon glyphicon-pencil"/>
                            </Button>)
                            : (null)
                        }
                    </div>
                </div>
                <div className="panel-body">
                    <div className="row">
                        <div className="col-md-3">
                            Estatus de Pre-Matricula:
                        </div>
                        <div className="col-md-3">
                            <p className="text-danger">{pre.requestStatusText}</p>
                        </div>
                        <div className="col-md-6"/>
                    </div>
                    <div className="row">
                        <div className="col-md-3">
                            Fecha de Solicitud:
                        </div>
                        <div className="col-md-9">
                            {(pre.submitDate && moment(pre.submitDate).format('LL, h:mm:ss a')) || "Aun no ha sido sometida"}
                        </div>
                    </div>
                    <div className="row">
                        <div className="col-md-3">
                            Matricula:
                        </div>
                        <div className="col-md-3">
                            {pre.type === "VOCATIONAL" ? "Vocacional" : "Regular"}
                        </div>
                        <div className="col-md-6"/>

                    </div>
                </div>

            </div>));
    }

}

function mapStateToProps(store) {
    return {
        preEnrollments: store.home.preEnrollments
    };
}

function mapDispatchToActions(dispatch) {
    return bindActionCreators({loadHome, resetWizard}, dispatch)
}

export default connect(mapStateToProps, mapDispatchToActions)(Home);



