import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './item-group.reducer';
import { IItemGroup } from 'app/shared/model/item-group.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IItemGroupDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ItemGroupDetail extends React.Component<IItemGroupDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { itemGroupEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="tpwebsiteApp.itemGroup.detail.title">ItemGroup</Translate> [<b>{itemGroupEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="itemGroupName">
                <Translate contentKey="tpwebsiteApp.itemGroup.itemGroupName">Item Group Name</Translate>
              </span>
            </dt>
            <dd>{itemGroupEntity.itemGroupName}</dd>
            <dt>
              <span id="itemGroupDescription">
                <Translate contentKey="tpwebsiteApp.itemGroup.itemGroupDescription">Item Group Description</Translate>
              </span>
            </dt>
            <dd>{itemGroupEntity.itemGroupDescription}</dd>
            <dt>
              <Translate contentKey="tpwebsiteApp.itemGroup.category">Category</Translate>
            </dt>
            <dd>{itemGroupEntity.category ? itemGroupEntity.category.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/item-group" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/item-group/${itemGroupEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ itemGroup }: IRootState) => ({
  itemGroupEntity: itemGroup.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ItemGroupDetail);
