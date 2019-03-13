import { Formik } from 'formik';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as Yup from 'yup';
import withResetOnNavigate from '../components/HOC/withResetOnNavigate';
import Loading from '../components/loading/Loading';
import {
  requestLeaveProfile,
  updateLeaveProfile
} from '../store/actions/leaveProfileActions';
import { getLeaveProfile } from '../store/selectors';

const validationSchema = Yup.object().shape({
  name: Yup.string().required(),
  entitlement: Yup.string().required(),
  maxCarriedOver: Yup.string().required(),
  maxBonusDays: Yup.string().required(),
  maxAllowanceFromNextYear: Yup.string().required(),
  training: Yup.string().required()
});

class EditLeaveProfile extends Component {
  componentDidMount() {
    this.props.requestLeaveProfile(this.props.match.params.id);
  }

  render() {
    if (!this.props.leaveProfile) return <Loading />;

    return (
      <div className="d-flex p-4 justify-content-center flex-column">
        <Formik
          validationSchema={validationSchema}
          initialValues={this.props.leaveProfile}
          onSubmit={this.props.updateLeaveProfile}
          enableReinitialize
          render={({ handleSubmit, handleChange, values, errors, touched }) => (
            <React.Fragment>
              <input
                className={`${
                  errors.name && touched.name ? 'border-danger' : ''
                } w-100 mb-3 border`}
                name="name"
                defaultValue={values.name}
                placeholder="Leave profile name"
                onChange={handleChange}
              />
              <input
                className={`${
                  errors.entitlement && touched.entitlement
                    ? 'border-danger'
                    : ''
                } w-100 mb-3 border`}
                name="entitlement"
                defaultValue={values.entitlement}
                placeholder="Entitlement"
                onChange={handleChange}
              />
              <input
                className={`${
                  errors.maxCarriedOver && touched.maxCarriedOver
                    ? 'border-danger'
                    : ''
                } w-100 mb-3 border`}
                name="maxCarriedOver"
                defaultValue={values.maxCarriedOver}
                placeholder="Max carried over"
                onChange={handleChange}
              />
              <input
                className={`${
                  errors.maxBonusDays && touched.maxBonusDays
                    ? 'border-danger'
                    : ''
                } w-100 mb-3 border`}
                name="maxBonusDays"
                defaultValue={values.maxBonusDays}
                placeholder="Max bonus days"
                onChange={handleChange}
              />
              <input
                className={`${
                  errors.maxAllowanceFromNextYear &&
                  touched.maxAllowanceFromNextYear
                    ? 'border-danger'
                    : ''
                } w-100 mb-3 border`}
                name="maxAllowanceFromNextYear"
                defaultValue={values.maxAllowanceFromNextYear}
                placeholder="Max allowance from next year"
                onChange={handleChange}
              />
              <input
                className={`${
                  errors.training && touched.training ? 'border-danger' : ''
                } w-100 mb-3 border`}
                name="training"
                defaultValue={values.training}
                placeholder="Max training"
                onChange={handleChange}
              />
              <textarea
                className="border mb-3"
                name="comment"
                defaultValue={values.comment}
                onChange={handleChange}
                placeholder="Comment"
              />
              <button className="btn" onClick={handleSubmit} type="submit">
                Update leave profile
              </button>
            </React.Fragment>
          )}
        />
      </div>
    );
  }
}

const mapStateToProps = state => ({
  leaveProfile: getLeaveProfile(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators({ requestLeaveProfile, updateLeaveProfile }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withResetOnNavigate()(EditLeaveProfile));
