import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tech-cv-docs.test-samples';

import { TechCVDocsFormService } from './tech-cv-docs-form.service';

describe('TechCVDocs Form Service', () => {
  let service: TechCVDocsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TechCVDocsFormService);
  });

  describe('Service methods', () => {
    describe('createTechCVDocsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTechCVDocsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            attachedDoc: expect.any(Object),
            technicalCV: expect.any(Object),
          }),
        );
      });

      it('passing ITechCVDocs should create a new form with FormGroup', () => {
        const formGroup = service.createTechCVDocsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            attachedDoc: expect.any(Object),
            technicalCV: expect.any(Object),
          }),
        );
      });
    });

    describe('getTechCVDocs', () => {
      it('should return NewTechCVDocs for default TechCVDocs initial value', () => {
        const formGroup = service.createTechCVDocsFormGroup(sampleWithNewData);

        const techCVDocs = service.getTechCVDocs(formGroup) as any;

        expect(techCVDocs).toMatchObject(sampleWithNewData);
      });

      it('should return NewTechCVDocs for empty TechCVDocs initial value', () => {
        const formGroup = service.createTechCVDocsFormGroup();

        const techCVDocs = service.getTechCVDocs(formGroup) as any;

        expect(techCVDocs).toMatchObject({});
      });

      it('should return ITechCVDocs', () => {
        const formGroup = service.createTechCVDocsFormGroup(sampleWithRequiredData);

        const techCVDocs = service.getTechCVDocs(formGroup) as any;

        expect(techCVDocs).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITechCVDocs should not enable id FormControl', () => {
        const formGroup = service.createTechCVDocsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTechCVDocs should disable id FormControl', () => {
        const formGroup = service.createTechCVDocsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
