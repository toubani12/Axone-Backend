import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../nda.test-samples';

import { NDAFormService } from './nda-form.service';

describe('NDA Form Service', () => {
  let service: NDAFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NDAFormService);
  });

  describe('Service methods', () => {
    describe('createNDAFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createNDAFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            document: expect.any(Object),
            status: expect.any(Object),
            period: expect.any(Object),
            employer: expect.any(Object),
            mediator: expect.any(Object),
            candidate: expect.any(Object),
          }),
        );
      });

      it('passing INDA should create a new form with FormGroup', () => {
        const formGroup = service.createNDAFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            document: expect.any(Object),
            status: expect.any(Object),
            period: expect.any(Object),
            employer: expect.any(Object),
            mediator: expect.any(Object),
            candidate: expect.any(Object),
          }),
        );
      });
    });

    describe('getNDA', () => {
      it('should return NewNDA for default NDA initial value', () => {
        const formGroup = service.createNDAFormGroup(sampleWithNewData);

        const nDA = service.getNDA(formGroup) as any;

        expect(nDA).toMatchObject(sampleWithNewData);
      });

      it('should return NewNDA for empty NDA initial value', () => {
        const formGroup = service.createNDAFormGroup();

        const nDA = service.getNDA(formGroup) as any;

        expect(nDA).toMatchObject({});
      });

      it('should return INDA', () => {
        const formGroup = service.createNDAFormGroup(sampleWithRequiredData);

        const nDA = service.getNDA(formGroup) as any;

        expect(nDA).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing INDA should not enable id FormControl', () => {
        const formGroup = service.createNDAFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewNDA should disable id FormControl', () => {
        const formGroup = service.createNDAFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
