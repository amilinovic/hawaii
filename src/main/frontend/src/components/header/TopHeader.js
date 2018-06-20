import React, { Component } from 'react';
import { Container, Row, Col } from 'reactstrap';
import { UserImage } from '../common/UserImage';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { requestApiData } from '../../store/actions/EmployeesActions';

class TopHeader extends Component {
  componentDidMount() {
    this.props.requestApiData();
  }

  render() {
    return (
      // TODO change mocked data with actual data
      <Container fluid>
        <Row className="justify-content-between align-items-end py-2">
          <Col xs="3">
            <img
              src="https://lh3.googleusercontent.com/Zeu3x2hWQxFtX6Mh8DYIgdqkkzN8EXI5C5regdIJm-lf0mCe2hKkQrs-kzSN7bTlY5IJbKuRI_N1Y5VLx6Z_0V51Ndk8fJ5oCIgmFg=h43"
              alt="hawaii logo"
            />
          </Col>
          <Col xs="3" className="text-center d-none d-lg-block">
            <img
              src="https://lh3.googleusercontent.com/Zeu3x2hWQxFtX6Mh8DYIgdqkkzN8EXI5C5regdIJm-lf0mCe2hKkQrs-kzSN7bTlY5IJbKuRI_N1Y5VLx6Z_0V51Ndk8fJ5oCIgmFg=h43"
              alt="execom logo"
            />
          </Col>
          <Col xs="3" className="justify-content-end d-none d-lg-inline-flex">
            <div className="d-inline-flex align-items-center">
              <UserImage
                image={
                  this.props.data.fetching === ''
                    ? 'none'
                    : this.props.data.data.results[0].picture.large
                }
              />
              {this.props.data.fetching === '' ? (
                <span>Fetching...</span>
              ) : (
                <div>
                  <span>{this.props.data.data.results[0].name.first}</span>
                </div>
              )}
            </div>
          </Col>
        </Row>
      </Container>
    );
  }
}

const mapStateToProps = state => ({ data: state.data });

const mapDispatchToProps = dispatch =>
  bindActionCreators({ requestApiData }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TopHeader);
