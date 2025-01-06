import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../app-test-type.test-samples';

import { AppTestTypeFormService } from './app-test-type-form.service';

describe('AppTestType Form Service', () => {
  let service: AppTestTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AppTestTypeFormService);
  });

  describe('Service methods', () => {
    describe('createAppTestTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAppTestTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            appTests: expect.any(Object),
          }),
        );
      });

      it('passing IAppTestType should create a new form with FormGroup', () => {
        const formGroup = service.createAppTestTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            appTests: expect.any(Object),
          }),
        );
      });
    });

    describe('getAppTestType', () => {
      it('should return NewAppTestType for default AppTestType initial value', () => {
        const formGroup = service.createAppTestTypeFormGroup(sampleWithNewData);

        const appTestType = service.getAppTestType(formGroup) as any;

        expect(appTestType).toMatchObject(sampleWithNewData);
      });

      it('should return NewAppTestType for empty AppTestType initial value', () => {
        const formGroup = service.createAppTestTypeFormGroup();

        const appTestType = service.getAppTestType(formGroup) as any;

        expect(appTestType).toMatchObject({});
      });

      it('should return IAppTestType', () => {
        const formGroup = service.createAppTestTypeFormGroup(sampleWithRequiredData);

        const appTestType = service.getAppTestType(formGroup) as any;

        expect(appTestType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAppTestType should not enable id FormControl', () => {
        const formGroup = service.createAppTestTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAppTestType should disable id FormControl', () => {
        const formGroup = service.createAppTestTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
