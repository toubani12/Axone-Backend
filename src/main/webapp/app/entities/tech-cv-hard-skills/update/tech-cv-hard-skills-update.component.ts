import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITechnicalCV } from 'app/entities/technical-cv/technical-cv.model';
import { TechnicalCVService } from 'app/entities/technical-cv/service/technical-cv.service';
import { ITechCVHardSkills } from '../tech-cv-hard-skills.model';
import { TechCVHardSkillsService } from '../service/tech-cv-hard-skills.service';
import { TechCVHardSkillsFormService, TechCVHardSkillsFormGroup } from './tech-cv-hard-skills-form.service';

@Component({
  standalone: true,
  selector: 'jhi-tech-cv-hard-skills-update',
  templateUrl: './tech-cv-hard-skills-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TechCVHardSkillsUpdateComponent implements OnInit {
  isSaving = false;
  techCVHardSkills: ITechCVHardSkills | null = null;

  technicalCVSSharedCollection: ITechnicalCV[] = [];

  protected techCVHardSkillsService = inject(TechCVHardSkillsService);
  protected techCVHardSkillsFormService = inject(TechCVHardSkillsFormService);
  protected technicalCVService = inject(TechnicalCVService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TechCVHardSkillsFormGroup = this.techCVHardSkillsFormService.createTechCVHardSkillsFormGroup();

  compareTechnicalCV = (o1: ITechnicalCV | null, o2: ITechnicalCV | null): boolean => this.technicalCVService.compareTechnicalCV(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ techCVHardSkills }) => {
      this.techCVHardSkills = techCVHardSkills;
      if (techCVHardSkills) {
        this.updateForm(techCVHardSkills);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const techCVHardSkills = this.techCVHardSkillsFormService.getTechCVHardSkills(this.editForm);
    if (techCVHardSkills.id !== null) {
      this.subscribeToSaveResponse(this.techCVHardSkillsService.update(techCVHardSkills));
    } else {
      this.subscribeToSaveResponse(this.techCVHardSkillsService.create(techCVHardSkills));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITechCVHardSkills>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(techCVHardSkills: ITechCVHardSkills): void {
    this.techCVHardSkills = techCVHardSkills;
    this.techCVHardSkillsFormService.resetForm(this.editForm, techCVHardSkills);

    this.technicalCVSSharedCollection = this.technicalCVService.addTechnicalCVToCollectionIfMissing<ITechnicalCV>(
      this.technicalCVSSharedCollection,
      techCVHardSkills.technicalCV,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.technicalCVService
      .query()
      .pipe(map((res: HttpResponse<ITechnicalCV[]>) => res.body ?? []))
      .pipe(
        map((technicalCVS: ITechnicalCV[]) =>
          this.technicalCVService.addTechnicalCVToCollectionIfMissing<ITechnicalCV>(technicalCVS, this.techCVHardSkills?.technicalCV),
        ),
      )
      .subscribe((technicalCVS: ITechnicalCV[]) => (this.technicalCVSSharedCollection = technicalCVS));
  }
}
