package tiffit.dungeondimension;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureOceanMonument;
import net.minecraft.world.gen.structure.StructureStart;

public class CustomMonumentStruct extends StructureOceanMonument {

	protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ){
		return chunkX == 0 && chunkZ == 0;
	}
	
}
