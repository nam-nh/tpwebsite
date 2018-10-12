import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './news.reducer';
import { INews } from 'app/shared/model/news.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface INewsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class NewsDetail extends React.Component<INewsDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { newsEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="tpwebsiteApp.news.detail.title">News</Translate> [<b>{newsEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="newsTitle">
                <Translate contentKey="tpwebsiteApp.news.newsTitle">News Title</Translate>
              </span>
            </dt>
            <dd>{newsEntity.newsTitle}</dd>
            <dt>
              <span id="newsContent">
                <Translate contentKey="tpwebsiteApp.news.newsContent">News Content</Translate>
              </span>
            </dt>
            <dd>{newsEntity.newsContent}</dd>
          </dl>
          <Button tag={Link} to="/entity/news" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/news/${newsEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ news }: IRootState) => ({
  newsEntity: news.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(NewsDetail);
