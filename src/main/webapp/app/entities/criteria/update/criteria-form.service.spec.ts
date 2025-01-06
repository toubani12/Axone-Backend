import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../criteria.test-samples';

import { CriteriaFormService } from './criteria-form.service';

describe('Criteria Form Service', () => {
  let service: CriteriaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CriteriaFormService);
  });

  describe('Service methods', () => {
    describe('createCriteriaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCriteriaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            applications: expect.any(Object),
          }),
        );
      });

      it('passing ICriteria should create a new form with FormGroup', () => {
        const formGroup = service.createCriteriaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            applications: expect.any(Object),
          }),
        );
      });
    });

    describe('getCriteria', () => {
      it('should return NewCriteria for default Criteria initial value', () => {
        const formGroup = service.createCriteriaFormGroup(sampleWithNewData);

        const criteria = service.getCriteria(formGroup) as any;

        expect(criteria).toMatchObject(sampleWithNewData);
      });

      it('should return NewCriteria for empty Criteria initial value', () => {
        const formGroup = service.createCriteriaFormGroup();

        const criteria = service.getCriteria(formGroup) as any;

        expect(criteria).toMatchObject({});
      });

      it('should return ICriteria', () => {
        const formGroup = service.createCriteriaFormGroup(sampleWithRequiredData);

        const criteria = service.getCriteria(formGroup) as any;

        expect(criteria).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICriteria should not enable id FormControl', () => {
        const formGroup = service.createCriteriaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCriteria should disable id FormControl', () => {
        const formGroup = service.createCriteriaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
