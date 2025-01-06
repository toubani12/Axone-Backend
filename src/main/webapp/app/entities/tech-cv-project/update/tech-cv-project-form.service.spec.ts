import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tech-cv-project.test-samples';

import { TechCVProjectFormService } from './tech-cv-project-form.service';

describe('TechCVProject Form Service', () => {
  let service: TechCVProjectFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TechCVProjectFormService);
  });

  describe('Service methods', () => {
    describe('createTechCVProjectFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTechCVProjectFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            project: expect.any(Object),
            technicalCV: expect.any(Object),
          }),
        );
      });

      it('passing ITechCVProject should create a new form with FormGroup', () => {
        const formGroup = service.createTechCVProjectFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            project: expect.any(Object),
            technicalCV: expect.any(Object),
          }),
        );
      });
    });

    describe('getTechCVProject', () => {
      it('should return NewTechCVProject for default TechCVProject initial value', () => {
        const formGroup = service.createTechCVProjectFormGroup(sampleWithNewData);

        const techCVProject = service.getTechCVProject(formGroup) as any;

        expect(techCVProject).toMatchObject(sampleWithNewData);
      });

      it('should return NewTechCVProject for empty TechCVProject initial value', () => {
        const formGroup = service.createTechCVProjectFormGroup();

        const techCVProject = service.getTechCVProject(formGroup) as any;

        expect(techCVProject).toMatchObject({});
      });

      it('should return ITechCVProject', () => {
        const formGroup = service.createTechCVProjectFormGroup(sampleWithRequiredData);

        const techCVProject = service.getTechCVProject(formGroup) as any;

        expect(techCVProject).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITechCVProject should not enable id FormControl', () => {
        const formGroup = service.createTechCVProjectFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTechCVProject should disable id FormControl', () => {
        const formGroup = service.createTechCVProjectFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
