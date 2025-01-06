import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../request.test-samples';

import { RequestFormService } from './request-form.service';

describe('Request Form Service', () => {
  let service: RequestFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RequestFormService);
  });

  describe('Service methods', () => {
    describe('createRequestFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRequestFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            status: expect.any(Object),
            expressionOfInterest: expect.any(Object),
            relatedApplication: expect.any(Object),
            recruiter: expect.any(Object),
          }),
        );
      });

      it('passing IRequest should create a new form with FormGroup', () => {
        const formGroup = service.createRequestFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            status: expect.any(Object),
            expressionOfInterest: expect.any(Object),
            relatedApplication: expect.any(Object),
            recruiter: expect.any(Object),
          }),
        );
      });
    });

    describe('getRequest', () => {
      it('should return NewRequest for default Request initial value', () => {
        const formGroup = service.createRequestFormGroup(sampleWithNewData);

        const request = service.getRequest(formGroup) as any;

        expect(request).toMatchObject(sampleWithNewData);
      });

      it('should return NewRequest for empty Request initial value', () => {
        const formGroup = service.createRequestFormGroup();

        const request = service.getRequest(formGroup) as any;

        expect(request).toMatchObject({});
      });

      it('should return IRequest', () => {
        const formGroup = service.createRequestFormGroup(sampleWithRequiredData);

        const request = service.getRequest(formGroup) as any;

        expect(request).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRequest should not enable id FormControl', () => {
        const formGroup = service.createRequestFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRequest should disable id FormControl', () => {
        const formGroup = service.createRequestFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
