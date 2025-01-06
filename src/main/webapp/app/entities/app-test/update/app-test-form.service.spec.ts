import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../app-test.test-samples';

import { AppTestFormService } from './app-test-form.service';

describe('AppTest Form Service', () => {
  let service: AppTestFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AppTestFormService);
  });

  describe('Service methods', () => {
    describe('createAppTestFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAppTestFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            invitationLink: expect.any(Object),
            totalScore: expect.any(Object),
            status: expect.any(Object),
            completed: expect.any(Object),
            testID: expect.any(Object),
            algorithm: expect.any(Object),
            isCodeTest: expect.any(Object),
            duration: expect.any(Object),
            types: expect.any(Object),
          }),
        );
      });

      it('passing IAppTest should create a new form with FormGroup', () => {
        const formGroup = service.createAppTestFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            invitationLink: expect.any(Object),
            totalScore: expect.any(Object),
            status: expect.any(Object),
            completed: expect.any(Object),
            testID: expect.any(Object),
            algorithm: expect.any(Object),
            isCodeTest: expect.any(Object),
            duration: expect.any(Object),
            types: expect.any(Object),
          }),
        );
      });
    });

    describe('getAppTest', () => {
      it('should return NewAppTest for default AppTest initial value', () => {
        const formGroup = service.createAppTestFormGroup(sampleWithNewData);

        const appTest = service.getAppTest(formGroup) as any;

        expect(appTest).toMatchObject(sampleWithNewData);
      });

      it('should return NewAppTest for empty AppTest initial value', () => {
        const formGroup = service.createAppTestFormGroup();

        const appTest = service.getAppTest(formGroup) as any;

        expect(appTest).toMatchObject({});
      });

      it('should return IAppTest', () => {
        const formGroup = service.createAppTestFormGroup(sampleWithRequiredData);

        const appTest = service.getAppTest(formGroup) as any;

        expect(appTest).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAppTest should not enable id FormControl', () => {
        const formGroup = service.createAppTestFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAppTest should disable id FormControl', () => {
        const formGroup = service.createAppTestFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
